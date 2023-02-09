import java.util.ArrayList;
import java.util.Stack;

public class PlaylistTree {
	public PlaylistNode primaryRoot;		//root of the primary B+ tree
	public PlaylistNode secondaryRoot;	//root of the secondary B+ tree
	public PlaylistTree(Integer order) {
		PlaylistNode.order = order;
		primaryRoot = new PlaylistNodePrimaryLeaf(null);
		primaryRoot.level = 0;
		secondaryRoot = new PlaylistNodeSecondaryLeaf(null);
		secondaryRoot.level = 0;
	}
	public void addSong(CengSong song) {
		// TODO: Implement this method
		// Primary B+ Tree
		PlaylistNodePrimaryLeaf InsertLEAF = primaryIndex(song.audioId());
		ArrayList<CengSong> songsOfLeaf = InsertLEAF.getSongs();
		int ind = InsertLEAF.songCount();
		int k, l;
		k=0;
		while(k < InsertLEAF.songCount())
		{
			if(songsOfLeaf.get(k).audioId() > song.audioId())
			{
				ind = k;
				break;}
			k++;}
		InsertLEAF.addSong(ind, song);
		if(InsertLEAF.getParent() != null)
		{
			if(InsertLEAF.songCount() == (2 * PlaylistNode.order) + 1)
			{PlaylistNode parent = InsertLEAF.getParent();
				PlaylistNodePrimaryLeaf newNode = new PlaylistNodePrimaryLeaf(parent);
				int order = PlaylistNode.order;
				k = 0;
				while(order < InsertLEAF.songCount())
				{
					newNode.addSong(k, InsertLEAF.songAtIndex(order));
					k++;
					order++;
				}
				InsertLEAF.getSongs().subList(PlaylistNode.order, InsertLEAF.getSongs().size()).clear();
				Integer Idnext = newNode.audioIdAtIndex(0);
				boolean boolval = false;
				k=0;
				while(k < ((PlaylistNodePrimaryIndex)parent).audioIdCount() )
				{
					if (((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(k) > Idnext)
					{((PlaylistNodePrimaryIndex)parent).addAudioId(k, Idnext);
						((PlaylistNodePrimaryIndex)parent).addChild(k + 1, newNode);
						boolval = true;
						break;
					}
					k++;
				}
				if(!boolval)
				{
					((PlaylistNodePrimaryIndex)parent).addAudioId(Idnext);
					((PlaylistNodePrimaryIndex)parent).addChild(newNode);
				}
				PlaylistNode nextParent;
				while (((PlaylistNodePrimaryIndex)parent).audioIdCount() == (2 * PlaylistNode.order) + 1)
				{boolval = false;
					nextParent = parent.getParent();
					if (nextParent != null)
					{
						PlaylistNodePrimaryIndex newInternalNode = new PlaylistNodePrimaryIndex(nextParent);
						int wanted;
						for (wanted = PlaylistNode.order + 1, l = 0 ; wanted < ((PlaylistNodePrimaryIndex)parent).audioIdCount() ; wanted++,l++)
						{newInternalNode.addAudioId(l,((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(wanted));
							newInternalNode.addChild(l,((PlaylistNodePrimaryIndex)parent).getChildrenAt(wanted));
							((PlaylistNodePrimaryIndex)parent).getChildrenAt(wanted).setParent(newInternalNode);
						}
						newInternalNode.addChild(newInternalNode.audioIdCount(),((PlaylistNodePrimaryIndex)parent).getChildrenAt(((PlaylistNodePrimaryIndex)parent).audioIdCount()));
						((PlaylistNodePrimaryIndex)parent).getChildrenAt(((PlaylistNodePrimaryIndex)parent).audioIdCount()).setParent(newInternalNode);
						Idnext = ((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(PlaylistNode.order);
						((PlaylistNodePrimaryIndex)parent).getAllIds().subList(PlaylistNode.order, ((PlaylistNodePrimaryIndex)parent).getAllIds().size()).clear();
						((PlaylistNodePrimaryIndex)parent).getAllChildren().subList(PlaylistNode.order + 1, ((PlaylistNodePrimaryIndex)parent).getAllChildren().size()).clear();
						k= 0;
						while ( k < ((PlaylistNodePrimaryIndex)nextParent).audioIdCount()) {
							if (((PlaylistNodePrimaryIndex)nextParent).audioIdAtIndex(k) > Idnext)
							{((PlaylistNodePrimaryIndex)nextParent).addAudioId(k, Idnext);
								((PlaylistNodePrimaryIndex)nextParent).addChild(k, parent);
								((PlaylistNodePrimaryIndex)nextParent).setChild(k + 1, newInternalNode);
								boolval = true;
								break;}
							k++;}
						if (!boolval) {
							((PlaylistNodePrimaryIndex)nextParent).addAudioId(Idnext);
							((PlaylistNodePrimaryIndex)nextParent).addChild(newInternalNode);}
						parent = nextParent;}
					else {
						PlaylistNodePrimaryIndex newRoot = new PlaylistNodePrimaryIndex(null);
						PlaylistNodePrimaryIndex newInternalNode = new PlaylistNodePrimaryIndex(newRoot);
						int need;
						l=0;
						need=PlaylistNode.order + 1;
						while( need < ((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(need)) {
							newInternalNode.addAudioId(l,((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(need));
							newInternalNode.addChild(l,((PlaylistNodePrimaryIndex)parent).getChildrenAt(need));
							((PlaylistNodePrimaryIndex)parent).getChildrenAt(need).setParent(newInternalNode);
							l++;
							need++;}
						newInternalNode.addChild(newInternalNode.audioIdCount(),((PlaylistNodePrimaryIndex)parent).getChildrenAt(((PlaylistNodePrimaryIndex)parent).audioIdCount()));
						((PlaylistNodePrimaryIndex)parent).getChildrenAt(((PlaylistNodePrimaryIndex)parent).audioIdCount()).setParent(newInternalNode);
						Idnext = ((PlaylistNodePrimaryIndex)parent).audioIdAtIndex(PlaylistNode.order);
						((PlaylistNodePrimaryIndex)parent).getAllIds().subList(PlaylistNode.order, ((PlaylistNodePrimaryIndex)parent).getAllIds().size()).clear();
						((PlaylistNodePrimaryIndex)parent).getAllChildren().subList(PlaylistNode.order + 1, ((PlaylistNodePrimaryIndex)parent).getAllChildren().size()).clear();
						newRoot.addAudioId(0, Idnext);
						newRoot.addChild(0, parent);
						parent.setParent(newRoot);
						newRoot.addChild(1, newInternalNode);
						primaryRoot = newRoot;
						break;}
				}
			}
		}

		else
		{if(InsertLEAF.songCount() == (2 * PlaylistNode.order) + 1)
		{
			PlaylistNodePrimaryIndex newRoot = new PlaylistNodePrimaryIndex(null);
			PlaylistNodePrimaryLeaf newNode = new PlaylistNodePrimaryLeaf(newRoot);
			int order = PlaylistNode.order;
			k = 0;
			while (order < InsertLEAF.songCount())
			{
				newNode.addSong(k, InsertLEAF.songAtIndex(order));
				order++;
				k++;
			}
			InsertLEAF.getSongs().subList(PlaylistNode.order, InsertLEAF.getSongs().size()).clear();
			InsertLEAF.setParent(newRoot);
			newRoot.addAudioId(0, newNode.songAtIndex(0).audioId());
			newRoot.addChild(0,InsertLEAF);
			newRoot.addChild(1, newNode);
			primaryRoot = newRoot;
		}
		}

	}
	public CengSong searchSong(Integer audioId) {
		PlaylistNode node = primaryRoot;
		boolean cont;
		int counter = -1;
		while (node.getType() == PlaylistNodeType.Internal)
		{
			counter++;
			System.out.println("\t".repeat(counter) +"<index>");
			int i=0;
			while ( i < ((PlaylistNodePrimaryIndex) node).audioIdCount()) {
				System.out.println("\t".repeat(counter) + ((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i));
				i++;
			}
			cont = false;
			System.out.println("\t".repeat(counter) + "</index>");

			for (i = 0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount() ; i++) {
				if (((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i) > audioId)
				{node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(i);
					cont = true;
					break;}}
			if (!cont)
				node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(((PlaylistNodePrimaryIndex) node).audioIdCount());
		}
		counter++;
		int i=0;
		while ( i < ((PlaylistNodePrimaryLeaf) node).songCount()) {
			if (((PlaylistNodePrimaryLeaf) node).audioIdAtIndex(i)== audioId)
			{
				System.out.println("\t".repeat(counter) + "<data>");
				System.out.print("\t".repeat(counter) + "<record>");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).audioId());
				System.out.print("|");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).genre());
				System.out.print("|");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).songName());
				System.out.print("|");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).artist());
				System.out.print("</record>\n");
				System.out.println("\t".repeat(counter) + "</data>");
				return (((PlaylistNodePrimaryLeaf) node).songAtIndex(i));}i++;}
		System.out.print("No match for ");
		System.out.println(audioId);
		return null;}


	public void printPrimaryPlaylist() {
		Stack<PlaylistNode> StackOfNodes = new Stack<>();
		PlaylistNode printer = primaryRoot;
		StackOfNodes.add(printer);
		int counter = -1;
		boolean oldOne = false;
		while (!StackOfNodes.isEmpty())
		{
			PlaylistNode node = StackOfNodes.pop();
			if (node.getType() == PlaylistNodeType.Leaf)
			{
				counter++;
				System.out.println("\t".repeat(counter)+ "<data>");

				for (int i = 0; i < ((PlaylistNodePrimaryLeaf) node).songCount() ; i++)
				{
					System.out.print("\t".repeat(counter)+"<record>");
					System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).audioId());
					System.out.print("|");
					System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).genre());
					System.out.print("|");
					System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).songName());
					System.out.print("|");
					System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).artist());
					System.out.print("</record>\n");}
				System.out.println("\t".repeat(counter)+"</data>");
				oldOne = true;
				counter--;}

			else if (node.getType() == PlaylistNodeType.Internal)
			{
				if(oldOne)
				{
					oldOne = false;
					counter--;
				}
				counter++;

				ArrayList<PlaylistNode> children = ((PlaylistNodePrimaryIndex) node).getAllChildren();
				int i = children.size() - 1;
				while (i >= 0){
					StackOfNodes.add(children.get(i));
					i--;}
				System.out.println("\t".repeat(counter)+ "<index>");
				for (i = 0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount() ; i++)
					System.out.println("\t".repeat(counter)+((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i));
				System.out.println("\t".repeat(counter)+ "</index>");}}
	}

	public void printSecondaryPlaylist() {
		Stack<PlaylistNode> StackOfNodes = new Stack<>();
		PlaylistNode printer = secondaryRoot;
		StackOfNodes.add(printer);
		int counter = -1;
		boolean oldOne = false;
		while (!StackOfNodes.isEmpty())
		{
			PlaylistNode node = StackOfNodes.pop();
			if (node.getType() == PlaylistNodeType.Internal)
			{
				if(oldOne)
				{
					oldOne = false;
					counter--;
				}
				counter++;

				ArrayList<PlaylistNode> children = ((PlaylistNodeSecondaryIndex) node).getAllChildren();
				int i = children.size() - 1;
				while (i >= 0){
					StackOfNodes.add(children.get(i));
					i--;}
				System.out.println("\t".repeat(counter)+ "<index>");
				for (i = 0; i < ((PlaylistNodeSecondaryIndex) node).genreCount() ; i++)
					System.out.println("\t".repeat(counter)+((PlaylistNodeSecondaryIndex) node).genreAtIndex(i));
				System.out.println("\t".repeat(counter)+ "</index>");
			}

			else if (node.getType() == PlaylistNodeType.Leaf)
			{
				counter++;
				System.out.println("\t".repeat(counter)+ "<data>");
				for (int i = 0; i < ((PlaylistNodeSecondaryLeaf) node).genreCount() ; i++)
				{ArrayList<CengSong> Songlist = ((PlaylistNodeSecondaryLeaf)node).getSongBucket().get(i);
					System.out.println("\t".repeat(counter)+ Songlist.get(0).genre());
					counter++;
					int t=0;
					while(t < Songlist.size())
					{
						System.out.print("\t".repeat(counter)+"<record>");
						System.out.print(Songlist.get(t).audioId());
						System.out.print("|");
						System.out.print(Songlist.get(t).genre());
						System.out.print("|");
						System.out.print(Songlist.get(t).songName());
						System.out.print("|");
						System.out.print(Songlist.get(t).artist());
						System.out.print("</record>\n");
						t++;
					}
					counter--;
				}
				System.out.println("\t".repeat(counter)+"</data>");
				oldOne = true;
				counter--;
			}
		}

	}

	public PlaylistNodePrimaryLeaf primaryIndex(Integer audioId) {

		PlaylistNode node = primaryRoot;
		boolean cont;
		while (node.getType() == PlaylistNodeType.Internal) {
			cont = false;
			int i;
			for (i=0;i < ((PlaylistNodePrimaryIndex) node).audioIdCount();i++) {

				if (((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i) > audioId) {
					node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(i);
					cont = true;
					break;
				}
			}
			if (!cont)
				node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(((PlaylistNodePrimaryIndex) node).audioIdCount());
		}
		return ((PlaylistNodePrimaryLeaf) node);
	}
}


