package com.yzc.solrJ.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class test {
	@Test
	public void updateIndex(){
			try {
				SolrServer solrServer = new HttpSolrServer("http://localhost:8383/solr");
				SolrInputDocument sid = new SolrInputDocument();
				sid.addField("id", "yzc");
				sid.addField("name", "don not block me");
				solrServer.add(sid);
				solrServer.commit();
				System.out.println("增加或修改索引成功");
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	
	@Test
	public void deleteIndex(){
		SolrServer solrServer = new HttpSolrServer("http://localhost:8383/solr");
		try {
			solrServer.deleteById("yzc");
			solrServer.commit();
			System.out.println("删除索引成功");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void queryIndex(){
		SolrServer solrServer = new HttpSolrServer("http://localhost:8383/solr");
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		try {
			QueryResponse query2 = solrServer.query(query);
			SolrDocumentList results = query2.getResults();
			System.out.println("id\tname\tprice");
			for (SolrDocument solrDocument : results) {
				System.out.println(solrDocument.get("id")+"\t"+solrDocument.get("product_name")+"\t"+solrDocument.get("product_price"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void queryIndex2(){
		SolrServer solrServer = new HttpSolrServer("http://localhost:8383/solr");
		SolrQuery query = new SolrQuery();
		query.setQuery("台灯");
		//设置默认域
		query.set("df", "product_name");
		//设置过滤条件
		query.addFilterQuery("product_price:[0 TO 20]");
		//设置排序条件
		query.setSort("product_price", ORDER.desc);
		//默认显示哪些域
		query.addField("id,product_name,product_price");
		
		//设置分页
		query.setStart(0);
		query.setRows(8);
		
		//设置高亮
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<span style='color:red'>");
		query.setHighlightSimplePost("</span>");
		
		
		
		try {
			QueryResponse query2 = solrServer.query(query);
			SolrDocumentList results = query2.getResults();
			System.out.println("id\tname\tprice");
			for (SolrDocument solrDocument : results) {
				System.out.println(solrDocument.get("id")+"\t"+solrDocument.get("product_name")+"\t"+solrDocument.get("product_price"));
		
				Map<String, Map<String, List<String>>> highlighting = query2.getHighlighting();
				String string = highlighting.get(solrDocument.get("id")).get("product_name").get(0);
				System.out.println("高亮的值："+string);
				
			}
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}
