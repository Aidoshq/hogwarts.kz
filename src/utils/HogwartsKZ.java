package utils;

import java.io.*;
import java.util.*;
import database.Database;
import enumerations.*;
import users.*;

public class HogwartsKZ {
	Language language = Language.ENG;
	User loggedUser;
	
	public HogwartsKZ() {
	}
	public void save() throws IOException {
		Database.write();
	}
	
	public void run() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int intIn;
		String strIn, strIn1;
		try {
				login :while(true) {
				System.out.println("🔮 Welcome to Hogwarts.kz, the Magical Network! 🔮 \n---------------------\n");	
				System.out.println("Please enter your username:");
				strIn = br.readLine();
				System.out.println("Please enter your password:");
				strIn1 = br.readLine();
				if(Database.DATA.getUsers().containsKey(new Credentials(strIn, strIn1))) {
					loggedUser=null;
					loggedUser=Database.DATA.getUsers().get(new Credentials(strIn, strIn1));
					System.out.println("You've logged succesfully");
					Database.DATA.getLogs().add(loggedUser + " logged into system at " + new Date());
					loggedUser.run();
				}else {
					System.out.println("Incorrect username of password. Please, try again.");
				}
			}
			
		}catch(Exception e) {
			System.out.println("Oopsiees");
			System.out.println(e.getMessage());
			save();
		}
	}
}
