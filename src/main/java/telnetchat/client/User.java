package telnetchat.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String name, passwordHash;
    private boolean isAdmin;
}
