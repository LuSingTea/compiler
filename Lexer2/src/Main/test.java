package Main;

import lexer.Lexer;
import lexer.Token;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        for (;;) {
            Token token = lexer.scan();
            OutputUtils.outputToken(token);
//            System.out.println(token);
        }
    }
}
