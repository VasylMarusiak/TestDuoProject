package utils.testrail;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.*;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.List;

import static java.lang.System.out;

@UtilityClass
public class TestRailUtil {

    public static int PROJECT_ID = 1;
    public static String TESTRAIL_USERNAME = "vasyamarusyak22@gmail.com";
    public static String TESTRAIL_PASSWORD = "wLI1NmHgz4s09Ud86NmA";
    public static String RAILS_ENGINE_URL = "https://testduoproject1.testrail.io/";

    public static String milestoneName = "Test_Milestone_Release_1.0";
    public static String runName = "AutomationRun_for_Milesone_1.0";
    private static final String projectName = "testDuoProject";

    public static final TestRail testRailClient = TestRail
            .builder(RAILS_ENGINE_URL, TESTRAIL_USERNAME, TESTRAIL_PASSWORD)
            .applicationName(projectName)
            .build();

    private static final List<ResultField> RESULT_FIELDS = testRailClient
            .resultFields()
            .list()
            .execute();

    public static int RUN_ID;


//    private static final List<Test> RUN_IDS = testRailClient
//            .tests()
//            .list(RUN_ID)
//            .execute();

//    public static TestRail getTestRailClient() {
//        var  testRailClient = TestRail
//                .builder(RAILS_ENGINE_URL, TESTRAIL_USERNAME, TESTRAIL_PASSWORD)
//                .applicationName(projectName)
//                .build();
//
//        return testRailClient;
//    }

    public static int getMilestoneId(String milestoneName) {
        return getMilestonesForProject(PROJECT_ID)
                .stream()
                .filter(milestone -> milestone
                        .getName()
                        .equalsIgnoreCase(milestoneName))
                .map(Milestone::getId)
                .findFirst()
                .orElse(0);
    }

    public static int milestoneId = TestRailUtil.getMilestoneId(milestoneName);

    public static List<Milestone> getMilestonesForProject(int projectId) {
        return testRailClient
                .milestones()
                .list(projectId)
                .execute();
    }

    public static List<Run> getRunsForProject(int projectId) {
        return testRailClient
                .runs()
                .list(projectId)
                .execute();
    }

    public static int getOrAddTestRun(String testRunName) {
        if (RUN_ID == 0) { //added to avoid fails, every run we well named unique
            //that is why we woun't have test runs with the same names, becuse if name exist we get it from
            //names list and add runs for this run
            RUN_ID = getRunsForProject(PROJECT_ID)
                    .stream()
                    .filter(run -> run.getName().equals(testRunName))
                    .map(Run::getId)
                    .findFirst()
                    .orElseGet(() -> addRun().getId());
        } else {
            var logMessage = "TestRail: using existing run with ID: " + RUN_ID;
            out.println(logMessage);
        }

        return RUN_ID;
    }

    @Step("TestRail: added {milestoneName} milestone to project ID {projectId}")
    public static Milestone addMilestone(int projectId, String milestoneName) {
        var milestone = new Milestone()
                .setProjectId(projectId)
                .setName(milestoneName);
        out.printf("TestRail: added %s milestone to project ID %s%n", milestoneName, projectId);

        return testRailClient
                .milestones()
                .add(projectId, milestone)
                .execute();
    }

    public static Run addRun() {
        Run run = testRailClient
                .runs()
                .add(PROJECT_ID, new Run().setName(runName))
                .execute();

        return run;
    }

    public static void setTestResult(ITestResult context, int id, int milestoneId) {

        var runWithAutomatedCasesOnly =  testRailClient
                .runs()
                .get(RUN_ID)
                .execute()
                .setMilestoneId(milestoneId);

        testRailClient
                .runs()
                .update(runWithAutomatedCasesOnly)
                .execute();

        testRailClient
                .results()
                .addForCase(id, getTestRailIdFromMethod(context), new Result()
                        .setStatusId(context.getStatus()), RESULT_FIELDS)
                .execute();
    }


//    public static int getTestRailFailCode(ITestResult context) {
//        System.out.println(context);
//        if (context.isSuccess()) {
//            System.out.println("all tests were success");
//            return TEST_CASE_PASSED_STATUS;
//        } else {
//            System.out.println("at least one test failed");
//
//            return TEST_CASE_FAILED_STATUS;
//        }
//    }

    public static int getTestRailIdFromMethod(ITestResult context) {
        Method[] methods = context.getTestClass().getRealClass().getDeclaredMethods();
        var annotationId = 0;
        for (Method method : methods) {
            if (method.isAnnotationPresent(TestRailCase.class)) {
                TestRailCase annotation = method.getAnnotation(TestRailCase.class);
                annotationId = annotation.id();
            }
        }
        return annotationId;
    }
}
