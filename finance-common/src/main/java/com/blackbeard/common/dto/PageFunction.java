package com.blackbeard.common.dto;


import org.springframework.stereotype.Repository;

@Repository
public class PageFunction {


	  public LimitPageDto getLimitPage(Page page,int count){
		    LimitPageDto limitPageDto = new LimitPageDto();
		    int totalPage =1;
		    if(count%page.getShowCount()==0){
				 totalPage = count/page.getShowCount();
			}else{
				 totalPage = count/page.getShowCount()+1;
			}
		 
			int star;
			int end;
			if(page.getCBCurrentPage()<=0){
				 star = 0;
				 end = page.getShowCount();
			}else if(page.getCBCurrentPage()>totalPage){
				 star =(totalPage-1)*page.getShowCount();
				 end = page.getShowCount();
				 page.setCurrentPage(totalPage);
			}else{
				 star = (page.getCBCurrentPage()-1)*page.getShowCount();
				 end = page.getShowCount();
			}
			limitPageDto.setStar(Integer.valueOf(star));
			limitPageDto.setEnd(Integer.valueOf(end));
			limitPageDto.setTotalPage(totalPage);
	        return limitPageDto;
	  }
	
	  public void setLimitPage(int listSize,int count,int totalPage,Page page){
		  if(!page.isEntityOrField()){
				page.setCurrentResult(page.getShowCount());
			}
			if(listSize==0){
				page.setTotalPage(0);
			}else{
				page.setTotalPage(totalPage);
			}
			if(page.getCBCurrentPage()==0||page.getCBCurrentPage()<0){
				page.setCurrentPage(1);
			}
			page.setEntityOrField(true);
			page.setTotalResult(count);
	  }
	  
	
}
