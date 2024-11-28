package com.flashcard.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseObject {

    private Object body;

    public static ResponseObject ok(Object body){
        return new ResponseObject(body);
    }
}
