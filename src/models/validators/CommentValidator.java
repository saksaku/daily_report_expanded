package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Comment;

public class CommentValidator {
    public static List<String> validate(Comment c) {
        List<String> errors = new ArrayList<String>();

        String content_error = _validateContent(c.getContent());
        if(!content_error.equals("")) {
            errors.add(content_error);
        }

        return errors;
    }

    private static String _validateContent(String content) {
        if(content == null || content.equals("")) {
            return "内容を入力してください。";
            }

        return "";
    }
}