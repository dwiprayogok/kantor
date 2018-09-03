package mandiri.finance.faith.Model;

public class News {
    String header,content,footer;
    public News(){}
    public News(String header,String content,String footer){
        this.content = content;
        this.footer = footer;
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public String getFooter() {
        return footer;
    }

    public String getHeader() {
        return header;
    }
}
