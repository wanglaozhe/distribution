package com.yuandong.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class StreamTest {
	private Logger logger = LoggerFactory.getLogger(getClass());

	 
	 public void testStream(){
//		 List<User> users = userService.findAll();
//		 for(int i=0;i<10;i++){
//			 User msg = new User();
//			 msg.setUserName("chenqi"+i);
//			 msg.setCreateBy("admin");
//			 msg.setCreateDate(new Date());
//			 msg.setUpdateBy("admin");
//			 msg.setUpdateDate(new Date());
//			 msg.setEmail("5343@qq.com");
//			 msg.setEmployedDates(new Date());
//			 msg.setPassword(100+i+"");
//			 users.add(msg);
//		 }
//		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 try {
//			 List<Date> ds = users.stream()
//				      .filter(u-> u.getUserName().equals("chenqi"))
//				      .sorted((u1,u2)->u2.getCreateDate().compareTo(u1.getCreateDate()))
//				      .map(u-> u.getCreateDate())
//				      .collect(Collectors.toList());
//				 ds.forEach(d->{
//					 logger.error("****************************");
//					 logger.error(sdf.format(d));
//					 logger.error("****************************");
//				 });
			 
			 //计算最长的字符长度
			 BufferedReader br = new BufferedReader(new FileReader("c:\\CrackCaptcha.log"));
			 /*int longest = br.lines().
					 flatMap(line->Stream.of(line.split("\n\r"))).
					 flatMap(line->Stream.of(line.split(" "))).
					 mapToInt(String::length).
					 max().
					 getAsInt();
			 System.out.println(longest);*/
			 
			 List<String> words = br.lines().
					 flatMap(line -> Stream.of(line.split(" "))).
					 filter(word -> word.length() > 0).
					 map(String::toLowerCase).
					 distinct().
					 sorted().
					 collect(Collectors.toList());
					br.close();
			System.out.println(words);
			br.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	 }
	 
	 
	 public void testMatch(){
		 List<Person> persons = new ArrayList();
		 persons.add(new Person(1, "name" + 1, 10));
		 persons.add(new Person(2, "name" + 2, 21));
		 persons.add(new Person(3, "name" + 3, 34));
		 persons.add(new Person(4, "name" + 4, 6));
		 persons.add(new Person(5, "name" + 5, 55));
		 boolean isAllAdult = persons.stream().
		  allMatch(p -> p.getAge() > 18);
		 System.out.println("All are adult? " + isAllAdult);
		 boolean isThereAnyChild = persons.stream().
		  anyMatch(p -> p.getAge() < 12);
		 System.out.println("Any child? " + isThereAnyChild);
	 }
	 
	 
	 public void testOptional(){
		 String text = "ggg";
		 Optional.ofNullable(text).ifPresent(System.out::println);
		System.out.println(Optional.ofNullable(text).map(String::length).orElse(-1));
	 }
	 
	 
	 public void testReduce(){
		// 字符串连接，concat = "ABCD"
		 String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat); 
		 System.out.println(concat);
		 concat = Stream.of("A", "B", "C", "D").reduce("",(a,b)->a.concat(b)); 
		 System.out.println(concat);
		 // 求最小值，minValue = -3.0
		 double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min); 
		 // 求和，sumValue = 10, 有起始值
		 int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
		 // 求和，sumValue = 10, 无起始值
		 sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
		 // 过滤，字符串连接，concat = "ace"
		 concat = Stream.of("a", "B", "c", "D", "e", "F").
		  filter(x -> x.compareTo("Z") > 0).
		  reduce("", String::concat);
	 }
	 
	 
	 public void testFlatMap(){
		 String[] arr = new String[]{"writer you\n\ryou are so beatuful"};
		 Stream.of(arr).
				 flatMap(line -> Stream.of(line.split("\n\r")))
				 .flatMap(line->Stream.of(line.split(" ")))
				 .filter(word -> word.length() > 0 && !StringUtils.isEmpty(word))
				 .peek(e->System.out.println("*********"+e))
//				 .collect(Collectors.toList())
				 .forEach(System.out::println);//peek一定是跟一個操作之前才會執行
	 }
	 
	 
	 public void testLimitAndSkip() {
		 List<Person> persons = new ArrayList();
		 for (int i = 1; i <= 10000; i++) {
		 Person person = new Person(i, "name" + i);
		 persons.add(person);
		 }
		List<String> personList2 = persons.stream()
				.map(Person::getName)
				.limit(10)
				.skip(3)
				.collect(Collectors.toList());
		 System.out.println(personList2);
		personList2 = persons.stream().limit(2).sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).map(Person::getName).collect(Collectors.toList());
		System.out.println(personList2);
	}
	 
	@Data
	private class Person {
		 public int no;
		 private String name;
		 private int age;
		 
		public Person(int no, String name, int age) {
			super();
			this.no = no;
			this.name = name;
			this.age = age;
		}

		public Person(int no, String name) {
			super();
			this.no = no;
			this.name = name;
		}
	}
	
	@Test
	public void testGenerate(){
		long time = System.currentTimeMillis();
//		Stream.generate(new PersonSupplier()).
//		limit(10).
//		forEach(p -> System.out.println(p.getName() + ", " + p.getAge()));
		int tmp = 0;
//		Map<Integer, List<Person>> personGroups = Stream.generate(new PersonSupplier()).
//				 limit(100000).
//				 collect(Collectors.groupingBy(Person::getAge));
		
		//查看每一个线程处理几个任务
		Map<Long, List<Long>> personGroups = Stream.generate(new PersonSupplier()).
				 limit(100000)
				 .parallel()//有此句代码表明用并行方式运行
				 .map(p->Thread.currentThread().getId()).
				 collect(Collectors.groupingBy(id->id));
				Iterator it = personGroups.entrySet().iterator();
				while (it.hasNext()) {
				 Map.Entry<Long, List<Long>> persons = (Map.Entry) it.next();
				 System.out.println("threadId" + persons.getKey() + " = " + persons.getValue().size());
				}
		
		
//		 Map<Boolean, List<Person>> children = Stream.generate(new PersonSupplier()).
//						 limit(1000000).
//						 collect(Collectors.partitioningBy(p -> p.getAge() < 18));
//						System.out.println("Children number: " + children.get(true).size());
//						System.out.println("Adult number: " + children.get(false).size());
		 System.out.println(System.currentTimeMillis() - time);
	}
	private class PersonSupplier implements Supplier<Person> {
		private int index = 0;
		private Random random = new Random();
		@Override
		public Person get() {
		    return new Person(index++, "StormTestUser" + index, random.nextInt(100));
		}
	}
}
