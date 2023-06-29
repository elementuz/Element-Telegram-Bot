package dtos;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String chatId;
    private String username;
    private String phoneNumber;
    private Integer age;
    private Gender gender;
    private float height;
    private float weight;
    private float neck;
    private float waist;
    private float hip;
    private Activity activity;
    private Goal goal;
    public enum Gender{
        MALE, FEMALE
    }

    public UserDto(Update update){
        this.firstName = update.getMessage().getFrom().getFirstName();
        this.lastName = update.getMessage().getFrom().getLastName();
        this.username = update.getMessage().getFrom().getUserName();
        this.chatId = update.getMessage().getChatId().toString();
    }

    @Override
    public String toString(){
        return "Age: " + this.age
                + "\nWeight: " + this.weight
                + "\nHeight: " + this.height
                + "\nAge: " + this.age
                + "\nActivity: " + this.activity;
    }

}
