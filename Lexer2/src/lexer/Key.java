package lexer;

// 用来存放5个关键字
public class Key extends Token {
    public String lexeme = "";
    public Key(String s,int tag) {
        super(tag);
        this.lexeme = s;
    }

    public String toString() {
        return "<" + lexeme + "," + " key" + ">";
    }
}
