package com.iiitmk.permissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlPermission {

	String manifestpath="",srcpath="";
	static Set<String> perm;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//File fp=new File();
		//perm=new HashSet<>();
		String ol=parseXML("E:/Dataset/testdata/test2m/resource/AndroidManifest.xml");
		//classChecker(ol,"E:/Dataset/testdata/test2m/javacode");
		//writeDataset(perm);
		
		writeXmlData(ol);

	}
	
	//parsing xml
	public static String parseXML(String fpath)
	{
		StringBuilder permission=new StringBuilder();
		try {
			File fp=new File(fpath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fp);
			doc.getDocumentElement().normalize();
			System.out.println("Root element :"+ doc.getDocumentElement().getNodeName());
		    NodeList nList = doc.getElementsByTagName("uses-permission");
		    System.out.println("----------------------------");
		    for (int temp = 0; temp < nList.getLength(); temp++)
		    {
		    	 Node nNode = nList.item(temp);
		    	// System.out.println("\nCurrent Element :"+ nNode.getNodeName());
		    	 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		               Element eElement = (Element) nNode;
		              // System.out.println("Permissions " +eElement.getAttribute("android:name"));
		               
		               permission.append("#"+eElement.getAttribute("android:name"));
		    	 }
		    }
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("ParserConfiguration Exception");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("SAX Exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("IO Exception");
		}
		
		//System.out.println("Permission Collected"+permission.toString());
		
		return permission.toString();
		
	}
	
	//checking corressponding class used
	public static void classChecker(String patteren,String dictFile)
	{
		//declared permissions cal
		String[] cal=patteren.split("#");
		FileInputStream fis;
		String[] cla;
		try {
			//read selected class files for permission
			fis = new FileInputStream("c:\\Users\\iiitmk\\workspacejee\\PermissionBased\\src\\com\\iiitmk\\permissions\\list.txt");
			int cht;
			String target="";
			while((cht=fis.read())!=-1)
			{
				target+=(char)cht;
			}
			cla=target.split("\n");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		File f=new File(dictFile);
		File[] totF=f.listFiles();
		for(File g:totF)
		{
			if(g.isDirectory())
			{
				classChecker(patteren, g.getAbsolutePath());
			}
			else
			{
				StringBuffer sb=new StringBuffer();
				try {
					
					System.out.println("Reading "+g.getName());
				FileInputStream fist=new FileInputStream(g);
				int ch;
				while((ch=fist.read())!=-1)
				{
					sb.append((char)ch);
				}
				String content=sb.toString();	
				perm=new HashSet<>();	
					
					//System.out.println(""+sb.toString());
				//getting permissions and corrersponding 
				for(int i=0;i<cal.length;i++)
				{
					//System.out.println(" Permission "+cal[i]);
					if(cal[i].contains("INTERNET"))
					{
						if(content.contains("AsyncHttpClient")||
								sb.toString().contains("AsyncHttpResponseHandler")||
								sb.toString().contains("HttpClient")||
								sb.toString().contains("HttpResponse")||
								sb.toString().contains("HttpPost")||sb.toString().contains("HttpPost"))
						{
							perm.add(cal[i]);System.out.println("Got permission"+cal[i]);
						}
					}
					else if(cal[i].contains("ACCESS_FINE_LOCATION") )
					{
						if(content.contains("LocationManager")||sb.toString().contains("requestLocationUpdates"))
						{
							perm.add(cal[i]);//System.out.println("Got permission"+cal[i]);
						}
						
					}
					else if(cal[i].contains("ACCESS_WIFI_STATE") )
					{
						if(content.contains("LocationManager")||sb.toString().contains("requestLocationUpdates"))
						{
							perm.add(cal[i]);//System.out.println("Got permission"+cal[i]);
						}
						
					}
					else if(cal[i].contains("ACCESS_NETWORK_STATE") )
					{
						if(content.contains("LocationManager")||sb.toString().contains("requestLocationUpdates"))
						{
							perm.add(cal[i]);//System.out.println("Got permission"+cal[i]);
						}
						
					}
					else if(cal[i].contains("RECEIVE_SMS") )
					{
						if(content.contains("SmsManager"))
						{
							perm.add(cal[i]);//System.out.println("Got permission"+cal[i]);
						}
					}
					else if(cal[i].contains("SEND_SMS") )
					{
						if(content.contains("SmsManager")||sb.toString().contains("sendTextMessage")||sb.toString().contains("sendDataMessage"))
						{
							perm.add(cal[i]);//System.out.println("Got permission"+cal[i]);
						}
						
					}
					else if(cal[i].contains("READ_PHONE_STATE") )
					{
						if(content.contains("PhoneStateListener"))
						{
							perm.add(cal[i]);//System.out.println("Got permission"+cal[i]);
						}
						
					}
				
					else
					{
						
					}
					
						Iterator<String> iperm=perm.iterator();
						System.out.println("Length"+iperm.hasNext());						
				}
				
				
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
		
		
		
	}
	//write dataset
	
	public static void writeDataset(Set<String> ob)
	{
		
		System.out.println("Length "+perm.size());
		
		try {
			FileInputStream csvf=new FileInputStream("C:/Users/iiitmk/workspacejee/PermissionBased/src/com/iiitmk/permissions/permset.csv");
			int c;
			String f="";
			while((c=csvf.read())!=-1)
			{
				f+=(char)c;
			}
			String[] allperm=f.split("\n");
			System.out.println("Length "+allperm.length);
			ArrayList<String> jk=new ArrayList<>();
			for(int i=0;i<allperm.length;i++)
			{
				//System.out.println("Intializing"+i);
				jk.add(i, "0");
			}
			for(int i=0;i<allperm.length;i++)
			{
				//System.out.println("setting"+i);
				Iterator ite=ob.iterator();
				while(ite.hasNext())
				{
					
					String v=ite.next().toString();
					//System.out.print("current "+v+" "+allperm[i]);
					
					if(allperm[i].equals(ite.next().toString()))
					{
						jk.set(i, "1");
						//System.out.println("value seted"+i);
					}
				}
				System.out.println("End of loop"+i);
			
			}
			
			String result="";
			Iterator jki=jk.iterator();
			while(jki.hasNext())
			{
				result+=jki.next()+",";
			}
			System.out.println("Writing Dataset");
			PrintWriter p=new PrintWriter("C:/Users/iiitmk/workspacejee/PermissionBased/src/com/iiitmk/permissions/dataset.csv");
			p.println(result+"\n");
			p.close();
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	///parse xml and dataset
	public static void writeXmlData(String d)
	{
		FileInputStream csvf;
		try {
			csvf = new FileInputStream("C:/Users/iiitmk/workspacejee/PermissionBased/src/com/iiitmk/permissions/permset.csv");
			int c;
			String f="";
			while((c=csvf.read())!=-1)
			{
				f+=(char)c;
			}
			
			//System.out.println("file content  "+f);
			ArrayList<String> jkxml=new ArrayList<>();
			String[] allperm=f.split("\n");
			String[] xmlperm=d.split("#");
			
			for(int m=0;m<allperm.length;m++)
			{
				jkxml.add("0");
				System.out.println("array  "+allperm[m]);
			}
			
			
			
			for(int k=0;k<allperm.length;k++)
			{
				for(int l=0;l<xmlperm.length;l++)
				{System.out.println("F====F  "+allperm[k]+"S=======S "+xmlperm[l]+"===ppp");
					if(allperm[k].contains(xmlperm[l]))
					{
						jkxml.set(l, "1");
					}
				}
			}
			Iterator ijkxml=jkxml.iterator();
			String res="";
			while(ijkxml.hasNext())
			{
				res+=ijkxml.next().toString()+",";
			}
			PrintWriter p=new PrintWriter("C:/Users/iiitmk/workspacejee/PermissionBased/src/com/iiitmk/permissions/dataset.csv");
			p.println(res+"\n");
			p.close();
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

}
