package com.example.userservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Response<T> implements Serializable {

    @JsonProperty("status")
    @NonNull
    private Integer status;

    @JsonProperty("message")
    @NonNull
    private String message;

    @JsonProperty("data")
    private T data;

    @JsonProperty("total")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;


    public Response(@NonNull Integer status, @NonNull String message, T data, Long total) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.total = total;
    }

   
}
