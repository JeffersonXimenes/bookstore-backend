package br.com.bookstore.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Represents an auth object, when user authentication is performed")
public class Auth {

    @Schema(description = "Token that will be used to provide the use of existing services ", example = "OMITIDO.eyJzdWIiOiJqZWZmZXJzb24ueGltZW5lczExQG91dGxvb2suY29tIiwiaWF0IjoxNzQ3NDQ3NjU3LCJleHAiOjE3NDc0NTEyNTd9.Gsqhf-OMITIDO")
    private String token;

    @Schema(description = "Type type ", example = "Bearer")
    private String type = "Bearer ";

    @Schema(description = "Authenticated user name ", example = "Jefferson Ximenes")
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT-3")
    @Schema(description = "Token Expiration ", example = "2025-05-16T00:00:00")
    private Date expiration;

    @Schema(description = "Accesses that the user has ", example = "[USER]")
    private String access;

    public Auth(String token, String access, Date expiration, String name) {
        this.token = token;
        this.access = access;
        this.expiration = expiration;
        this.name = name;
    }
}