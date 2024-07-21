package lk.ijse.gdse.aad68.studentmanagement.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class StudentDTO {
    private String ID;
    private String Name;
    private String email;
    private String  city;
    private String level;
}

