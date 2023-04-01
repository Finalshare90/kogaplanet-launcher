package com.gmail.kogaplanetdev.kogaplanetlauncher;

import java.util.Random;

public class LLTJokes {
	
	//I don't even know why i didn't used a I/O reader in a separated file to do that, but who cares?
	
	String jokes[] = {"Rogerio tech","Waldir Braz","Watch out for James!",
					 "Did you know that to open a door, just open it",
					 "Pro Tip: Walk.", "Coming soon in: never!", "james is so forgotten that he doesn't"
					+" even have a package", "KoGaPlanet was originally a KoGaMa gaming site.",
					 "The first version of KoGaPlanet was made on Weebly", 
					 "Hope is the last to die and my patience programming this is the first."
				    +"(Final approves this)", "There is no better day than today"
					+" to leave for tomorrow what you will never do!",
					 "Programmer note: h e l p", 
					 "there are 2 things I don't know if they exist, quality in my code and aliens",
					 "did you know that Finalshare is coffee powered? the last coffee incident was very unpleasant.",
					 "It is a bird? is it an airplane? no, it's a goddamn bug in the android version",
					 "When you're in the fight, don't be useless", "If adam and eve fought would it be a world war?",
					 "If Pinocchio says that his nose will grow, what happens?", "3l was probably an excuse for Final"
					 + " not to have to add more content, who knows?", "LunarLutte Team warns: the game runs fine, at"
					 + "least on testing machines" + "Now with 3ml-core!"
					};
	Random rnd = new Random();
	
	
	public String chooseAjoke(){
		
		int randomNumber = rnd.nextInt(jokes.length);
		
		String selectedJoke = jokes[randomNumber];
		
		return selectedJoke;
	}

}
