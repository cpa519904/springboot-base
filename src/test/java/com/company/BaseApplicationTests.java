package com.company;

import com.company.common.third.wechat.domain.UnifiedOrderRequest;
import com.company.common.third.wechat.domain.UnifiedOrderResponse;
import com.company.common.tools.Constants;
import com.company.common.tools.Utils;
import com.company.common.tools.rabbitMQ.Publish;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseApplicationTests {

	@Autowired
	private Publish publish;

	@Test
	public void contextLoads() {
		publish.delayQueue(Constants.ORDER_CACHE_QUEUE_NAME,"one order");
		try {
			Thread.sleep(65*1000);
		}catch (Exception e){}
	}

	@Test
	public void xml() throws Exception{
		UnifiedOrderRequest request = new UnifiedOrderRequest();
		request.setSign("123");
		request.setNotify_url("456");
		request.setAppid("1");
		request.setAttach("2");
		request.setBody("3");
		request.setDetail("4");

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
				"<xml>\n" +
				"    <appid>1</appid>\n" +
				"    <attach>2</attach>\n" +
				"    <body>3</body>\n" +
				"    <detail>4</detail>\n" +
				"    <notify_url>456</notify_url>\n" +
				"    <sign>123</sign>\n" +
				"</xml>";

		//System.out.println(Utils.beanToXml(request, UnifiedOrderRequest.class));

		//System.out.println(Utils.xmlToBean(xml, UnifiedOrderResponse.class).toString());
	}
}
