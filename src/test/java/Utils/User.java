package Utils;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
        private String email;
        private String password;
}
