package net.iteach.service.comment

import org.junit.Test


class CommentHTMLFormatterTest {
	
	CommentFormatter f = new CommentHTMLFormatter()
	
	@Test
	void plain () {
		assert "Just some text" == f.format("Just some text")
	}
	
	@Test
	void carriage_returns () {
		assert "With a new<br/>line" == f.format("With a new\nline")
	}
	
	@Test
	void bold () {
		assert "With <b>bold</b> text" == f.format("With *bold* text")
	}
	
	@Test
	void bold_twice () {
		assert "With <b>bold</b> and <b>again bold</b> text" == f.format("With *bold* and *again bold* text")
	}
	
	@Test
	void italic () {
		assert "With <i>italic</i> text" == f.format("With _italic_ text")
	}
	
	@Test
	void italic_twice () {
		assert "With <i>italic</i> and <i>again italic</i> text" == f.format("With _italic_ and _again italic_ text")
	}
	
	@Test
	void italic_and_bold () {
		assert "With <i>italic</i> and <b>bold</b> text" == f.format("With _italic_ and *bold* text")
	}
	
	@Test
	void italic_in_bold () {
		assert "With <b>some <i>italic</i> in bold</b>" == f.format("With *some _italic_ in bold*")
	}
	
	@Test
	void italic_cross_return () {
		assert "With <i>some italic with a<br/>new line</i>" == f.format("With _some italic with a\nnew line_")
	}
	
	@Test
	void bold_cross_return () {
		assert "With <b>some bold with a<br/>new line</b>" == f.format("With *some bold with a\nnew line*")
	}
	
	@Test
	void link () {
		assert """With a <a href="http://en.wikipedia.org">http://en.wikipedia.org</a> link""" == f.format("With a http://en.wikipedia.org link")
	}
	
	@Test
	void complete_example () {
		assert """This a <i>sample of <b>text</b></i> that<br/>spans on several lines and includes<br/>a <b>link</b>: <a href="http://en.wikipedia.org">http://en.wikipedia.org</a> and<br/><b>even</b> another <a href="http://github.com">http://github.com</a><br/>close to the end with a... &lt;script&gt;alert('Hey! Got you!');&lt;/script&gt;.""" == f.format(
"""This a _sample of *text*_ that
spans on several lines and includes
a *link*: http://en.wikipedia.org and
*even* another http://github.com
close to the end with a... <script>alert('Hey! Got you!');</script>.""")
	}
	
	@Test
	void escape () {
		assert "With &lt;i&gt;italic&lt;/i&gt; text" == f.format("With <i>italic</i> text")
	}
	
}
