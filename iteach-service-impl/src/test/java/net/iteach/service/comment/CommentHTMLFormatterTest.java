package net.iteach.service.comment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentHTMLFormatterTest {

    private CommentFormatter f = new CommentHTMLFormatter();

    @Test
    public void plain() {
        assertEquals("Just some text", f.format("Just some text"));
    }

    @Test
    public void carriage_returns() {
        assertEquals("With a new<br/>line", f.format("With a new\nline"));
    }

    @Test
    public void bold() {
        assertEquals("With <b>bold</b> text", f.format("With *bold* text"));
    }

    @Test
    public void bold_twice() {
        assertEquals("With <b>bold</b> and <b>again bold</b> text", f.format("With *bold* and *again bold* text"));
    }

    @Test
    public void italic() {
        assertEquals("With <i>italic</i> text", f.format("With _italic_ text"));
    }

    @Test
    public void italic_twice() {
        assertEquals("With <i>italic</i> and <i>again italic</i> text", f.format("With _italic_ and _again italic_ text"));
    }

    @Test
    public void italic_and_bold() {
        assertEquals("With <i>italic</i> and <b>bold</b> text", f.format("With _italic_ and *bold* text"));
    }

    @Test
    public void italic_in_bold() {
        assertEquals("With <b>some <i>italic</i> in bold</b>", f.format("With *some _italic_ in bold*"));
    }

    @Test
    public void italic_cross_return() {
        assertEquals("With <i>some italic with a<br/>new line</i>", f.format("With _some italic with a\nnew line_"));
    }

    @Test
    public void bold_cross_return() {
        assertEquals("With <b>some bold with a<br/>new line</b>", f.format("With *some bold with a\nnew line*"));
    }

    @Test
    public void link() {
        assertEquals("With a <a href=\"http://en.wikipedia.org\">http://en.wikipedia.org</a> link", f.format("With a http://en.wikipedia.org link"));
    }

    @Test
    public void complete_example() {
        assertEquals("This a <i>sample of <b>text</b></i> that<br/>spans on several lines and includes<br/>a <b>link</b>: <a href=\"http://en.wikipedia.org\">http://en.wikipedia.org</a> and<br/><b>even</b> another <a href=\"http://github.com\">http://github.com</a><br/>close to the end with a... &lt;script&gt;alert('Hey! Got you!');&lt;/script&gt;.", f.format(
                "This a _sample of *text*_ that\nspans on several lines and includes\na *link*: http://en.wikipedia.org and\n*even* another http://github.com\nclose to the end with a... <script>alert('Hey! Got you!');</script>."));
    }

    @Test
    public void escape() {
        assertEquals("With &lt;i&gt;italic&lt;/i&gt; text", f.format("With <i>italic</i> text"));
    }

}
