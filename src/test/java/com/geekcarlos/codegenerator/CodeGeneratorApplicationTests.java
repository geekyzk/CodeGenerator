package com.geekcarlos.codegenerator;

import com.geekcarlos.codegenerator.model.EntityItemModel;
import com.geekcarlos.codegenerator.model.EntityModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CodeGeneratorApplicationTests {

	@Test
	public void contextLoads() {
		EntityModel entityModel = EntityModel.builder()
				.name("User")
				.tableName("sys_user")
				.build();
		System.out.println(entityModel);
	}

	@Test
	public void testEntityItemModel() {
		EntityItemModel entityItemModel = EntityItemModel.builder()
				.name("id")
				.columnName("id")
				.typeClass(String.class)
				.build();
		System.out.println(entityItemModel);
	}

}
