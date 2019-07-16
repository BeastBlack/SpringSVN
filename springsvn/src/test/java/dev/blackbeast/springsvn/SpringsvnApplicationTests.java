package dev.blackbeast.springsvn;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@SpringBootTest
class SpringsvnApplicationTests {

	@Test
	void contextLoads() {
		Pattern pattern = Pattern.compile("(TEET-)\\d{1,}");
		Matcher m = pattern.matcher("TEET-10000 abc");

		StringBuffer bufStr = new StringBuffer();

		boolean flag = false;
		while ((flag = m.find())) {
			String rep = m.group();
			m.appendReplacement(bufStr, "<a th:href=\"abc\">" + rep + "</a>");
		}

		m.appendTail(bufStr);
		String result = bufStr.toString();
		System.out.println(result);
	}

}
