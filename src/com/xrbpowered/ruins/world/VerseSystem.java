package com.xrbpowered.ruins.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.xrbpowered.ruins.ui.overlay.RomanNumerals;

public class VerseSystem {

	public static final String[] verses = {
		"Where can you go if you do not know where you come from",
		"Who would you become if you do not know who you are",
		"He looked at the stars and he saw a city full of life",
		"She seeked justice but all she found was judgement",
		"He did not know the right thing to do",
		"When he knew what to do he had no power to do that",
		"When he became strong it was too late",
		//"He gave his life to find his purpose but death rejected him",
		//"She gave her life for love and love made her immortal",
		//"Patience is a virtue of the dead",
		//"He is still looking for her among the stars",
		//"She is still waiting for him to come",
		"Can he be punished for following the path he chose unknowingly",
		"Can she be punished for following the path that was chosen for her",
		"They could have saved each other if they had found each other but they never did",
		"They can be forgiven because they can forgive",
		"They can be free because they can forget",
	};
	
	public HashSet<String> known = new HashSet<>();
	
	private Random random = new Random();
	
	public String learned = null;
	public int verse;
	public boolean complete = false;
	
	public boolean[] completeVerses = new boolean[verses.length];
	
	public void load(DataInputStream in) throws IOException {
		for(int i=0; i<verses.length; i++)
			completeVerses[i] = in.readBoolean();
		int num = in.readInt();
		for(int i=0; i<num; i++) {
			known.add(in.readUTF());
		}
	}
	
	public void save(DataOutputStream out) throws IOException {
		for(int i=0; i<verses.length; i++)
			out.writeBoolean(completeVerses[i]);
		out.writeInt(known.size());
		for(String s : known) {
			out.writeUTF(s);
		}
	}
	
	public String access(long seed, boolean learn) {
		random.setSeed(seed);
		verse = random.nextInt(verses.length);
		String[] words = verses[verse].toUpperCase().split("\\s+");
		ArrayList<String> unknown = new ArrayList<>();
		for(String w : words) {
			if(!known.contains(w))
				unknown.add(w);
		}
		complete = unknown.isEmpty();
		learned = null;
		if(!complete && learn) {
			learned = unknown.get(random.nextInt(unknown.size()));
			known.add(learned);
			if(unknown.size()==1)
				complete = true;
		}
		if(complete)
			completeVerses[verse] = true;
		StringBuilder text = new StringBuilder();
		text.append("<p>- ");
		text.append(RomanNumerals.toRoman(verse+1));
		text.append(" -</p>");
		text.append("<p");
		text.append(complete ? "" : " class=\"dim\"");
		text.append(">");
		
		for(String w : words) {
			if(!known.contains(w))
				text.append("&bull;");
			else if(w.equals(learned))
				text.append("<span class=\"e\">"+w+"<span>");
			else
				text.append(w);
			text.append(" ");
		}
		
		text.append("</p>");
		return text.toString();
	}
}
