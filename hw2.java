import java.io.*;
import java.util.*;
import java.math.*;

public class hw2 {

	public static void main(String[] args) throws IOException {
			ArrayList<String> VarNames = new ArrayList<String>();
			ArrayList<ArrayList<String>> Levels = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> DataTrain = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> DataTest = new ArrayList<ArrayList<String>>();
			Double m = new Double(0);
			ArrayList<Double> md = new ArrayList<Double> ();
			readData(args, VarNames, Levels, DataTrain, DataTest, md);
			m = md.get(0);
			double t = m+2;
			t = t+3;
			//initialize DataTr
			ArrayList<ArrayList<String>> DataTr = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < Levels.size(); i++) {
				DataTr.add(new ArrayList<String>());
			}
			
			transformData(DataTrain, DataTr, Levels);

			DecisionTreeNode root = new DecisionTreeNode(null,null);
	
			DecisionTreeNode root1 = maketree(DataTr, root, Levels, VarNames, m, null);
			predict(root1, DataTest, VarNames);
			
//			//part2
//			if (m == 4) {
//				ArrayList<Double> percent = new ArrayList<Double> ();
//				percent.add(0.05);
//				percent.add(0.1);
//				percent.add(0.2);
//				percent.add(0.5);
//				ArrayList<String> totalratio = new ArrayList<String> ();
//				for (int j = 0; j < percent.size(); j++) {
//					ArrayList<Double> eachratio = new ArrayList<Double>();
//					for (int i = 0; i < 10; i ++) {
//						ArrayList<ArrayList<String>> percentData = new ArrayList<ArrayList<String>>();
//						try {
//							percentData = cutData(DataTr, percent.get(j));
//						} catch (Exception e) {
//							try {
//								percentData = cutData(DataTr, percent.get(j));
//							} catch (Exception e1) {
//								try {
//									percentData = cutData(DataTr, percent.get(j));
//								} catch (Exception e2) {
//									try {
//										percentData = cutData(DataTr, percent.get(j));
//									} catch (Exception e3) {
//										try {
//											percentData = cutData(DataTr, percent.get(j));
//										} catch (Exception e4) {
//											try {
//												percentData = cutData(DataTr, percent.get(j));
//											} catch (Exception e5) {
//												try {
//													percentData = cutData(DataTr, percent.get(j));
//												} catch (Exception e6) {
//													try {
//														percentData = cutData(DataTr, percent.get(j));
//													} catch (Exception e7) {
//														try {
//															percentData = cutData(DataTr, percent.get(j));
//														} catch (Exception e8) {
//															try {
//																percentData = cutData(DataTr, percent.get(j));
//															} catch (Exception e9) {
//																System.exit(0);
//															}
//														}
//													}
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//						DecisionTreeNode rootsub = new DecisionTreeNode(null,null);
//						DecisionTreeNode root2 = maketree2(percentData,rootsub, Levels, VarNames, m, null);
//						eachratio.add(predict2(root2, DataTest, VarNames));
//					}
//					totalratio.add(Double.toString(averageratio(eachratio)));
//					totalratio.add(Double.toString(maxratio(eachratio)));
//					totalratio.add(Double.toString(minratio(eachratio)));
//				}
//				System.out.println("");
//				System.out.println("part2");
//				for (int i = 0; i < totalratio.size(); i++) {
//					if (i % 3 == 0 && i != 0)
//						System.out.println("");
//					System.out.print(totalratio.get(i) + " ");
//				}
//				
//				try {
//				    FileOutputStream fos = new FileOutputStream("output.txt");
//				    ObjectOutputStream oos = new ObjectOutputStream(fos);   
//				    oos.writeObject(totalratio); // write MenuArray to ObjectOutputStream
//				    oos.close(); 
//				} catch(Exception ex) {
//				    ex.printStackTrace();
//				}
//				
//			}
//			
//			//part3
//			System.out.println("\n");
//			System.out.println("part3");
//			ArrayList<Double> allm = new ArrayList<Double> ();
//			allm.add(2.0);
//			allm.add(5.0);
//			allm.add(10.0);
//			allm.add(20.0);
//			for (int i = 0; i < allm.size(); i++) {
//				DecisionTreeNode rootm = maketree2(DataTr, root, Levels, VarNames, allm.get(i), null);
//				System.out.print(predict2(rootm, DataTest, VarNames) + " ");
//			}
	}
	
	public static Double averageratio(ArrayList<Double> Accuracy) {
		Double sum = 0.00;
		for (int s = 0; s < Accuracy.size(); s++) {
			sum += (double) Accuracy.get(s);
		}
		Double ave = Double.valueOf((double) sum / Accuracy.size());
		return ave;
	}
	
	public static Double maxratio(ArrayList<Double> Accuracy) {
		Double max = 0.00;
		for (int s = 0; s < Accuracy.size(); s++) {
			if (Accuracy.get(s) > max) {
				max = Accuracy.get(s);
			}
		}
		return max;
	}
	public static Double minratio(ArrayList<Double> Accuracy) {
		Double min = Accuracy.get(0);
		for (int s = 0; s < Accuracy.size(); s++) {
			if (Accuracy.get(s) < min) {
				min = Accuracy.get(s);
			}
		}
		return min;
	}
	
	
	public static void readData(String [] args, ArrayList<String> VarNames, ArrayList<ArrayList<String>> Levels, ArrayList<ArrayList<String>> DataTrain, ArrayList<ArrayList<String>> DataTest, ArrayList<Double> m){
		if (args.length != 3) {
            System.out.println("Usage: java -cp . CompanyHierarchyMain FileName");
            System.exit(0);
        }

        // *** Step 2: Check whether the input file exists and is readable ***
		File f = new File(args[0]);
		if (!f.exists() || !f.canRead()) {
			System.out.println("Error: Cannot access input file");
			System.exit(0);
		}
		try {	
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				String line = sc.nextLine();
				//first deal with variable names and categorical levels
				if (line.length() >10 && line.substring(0,10).equals("@attribute")) {
					//use kongge to split data
					String[] slines = line.split(" ");
					slines[1] = slines[1].replace("'", "");
					VarNames.add(slines[1]);
					ArrayList<String> newLevel = new ArrayList<String>();
					if (slines[2].equals("real") || slines[2].equals("numeric")) {
						newLevel.add("real");
						Levels.add(newLevel);
					}
					else {						
						for (int i = 3; i < slines.length; i++) {
							slines[i] = slines[i].replace(",", "");
							slines[i] = slines[i].replace("}", "");
							newLevel.add(slines[i]);
						}
						Levels.add(newLevel);
					}
				}
				if (!line.substring(0, 1).equals("@") && !line.substring(0,1).equals(" ") && !line.equals("")) {
					String[] slines1 = line.split(",");
					for (int i = 0; i < slines1.length; i++)
						slines1[i] = slines1[i].replace(" ", "");
					ArrayList<String> s1 = new ArrayList<String>();
					for (int i = 0; i < slines1.length; i++) {
						s1.add(slines1[i]);
					}
					DataTrain.add(s1);
				}
			}
		} catch(Exception e) {
			System.out.println("Cannot read file");
		}
		
		File f1 = new File(args[1]);
		if (!f1.exists() || !f1.canRead()) {
			System.out.println("Error: Cannot access input file");
			System.exit(0);
		}
		try {	
			Scanner sc = new Scanner(f1);
			while (sc.hasNext()) {
				String line = sc.nextLine();
				if (!line.substring(0, 1).equals("@")) {
					String[] slines1 = line.split(",");
					ArrayList<String> s1 = new ArrayList<String>();
					for (int i = 0; i < slines1.length; i++) {
						s1.add(slines1[i]);
					}
					DataTest.add(s1);
				}
			}
		} catch(Exception e) {
			System.out.println("Cannot read file");
		}
		
		m.add(Double.valueOf(args[2]));
		
	}
	
	//change dataset to a form that it is assigned by var.
	public static void transformData(ArrayList<ArrayList<String>> Data, ArrayList<ArrayList<String>> DataO, ArrayList<ArrayList<String>> Levels) {
		//Traverse all variables
		for (int j = 0; j < Data.size(); j ++) {
			for (int i = 0; i < DataO.size(); i++) {	
				DataO.get(i).add(Data.get(j).get(i));
			}
		}
	}
	
	
	public static ArrayList<ArrayList<String>> cutData(ArrayList<ArrayList<String>> Data, Double percent) {
		ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>();
		while (oneCa(a)) {
			for (int i = 0; i < Data.size(); i++) {
				a.add(new ArrayList<String>());
			}
			int totalsize = Data.get(0).size();
			double our = Math.floor((double) totalsize*percent);
			int oursize = (int) our;
			Object[] values = new Object[oursize];
		        
		    Random random = new Random();
		    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
		        
		    // get random and save to HashMap
		    int i = 0;
		    while(i < 6){
		        int number1 = random.nextInt(Data.get(0).size()-5)+1;
		        int number2 = random.nextInt(Data.get(0).size()-5)+1;
		        if (number1 != number2 && !Data.get(Data.size()-1).get(number1).equals(Data.get(Data.size()-1).get(number2)) && !hashMap.containsKey(number1) && !hashMap.containsKey(number2)) {
		        	hashMap.put(number1, 0);
		        	hashMap.put(number2, 1);
		        	i = 6;
		        }
		    }
		    while (i < values.length) {
		    	int number = random.nextInt(Data.get(0).size()) + 1;
		        if (!hashMap.containsKey(number)) {
		        	hashMap.put(number, i);
		        	i++;
		        }
		    } 
		    // load array from hashMap
		    values = hashMap.keySet().toArray();
		    
		    for (i = 0; i < values.length; i++) {
		    	for (int j = 0; j < Data.size(); j++) {
		    		int t = (int) values[i];
		    		String s = Data.get(j).get(t);
		    		a.get(j).add(s);    
		    	}
		    }
		}
		return a;
	}
	
	public static void predict (DecisionTreeNode root, ArrayList<ArrayList<String>> DataTest, ArrayList<String> VarNames) {
		int count = 0;
		String ss ="\n"; 
		ss += "<Predictions for the Test Set Instances>";
		System.out.println(ss);
		for (int i = 0; i < DataTest.size(); i++) {
			String s = Integer.toString(i+1);
			s += ": Actual: ";
			s += DataTest.get(i).get(DataTest.get(i).size()-1);
			s += " Predicted: ";
			s += predictRe(root, DataTest.get(i), VarNames);
			if (predictRe(root, DataTest.get(i), VarNames).equals(DataTest.get(i).get(DataTest.get(i).size()-1)))
				count++;
			System.out.println(s);
		}
		String s = "Number of correctly classified: ";
		s += count;
		s += " Total number of test instances: ";
		s += Integer.toString(DataTest.size());
		System.out.println(s);
	}
	
	public static Double predict2 (DecisionTreeNode root, ArrayList<ArrayList<String>> DataTest, ArrayList<String> VarNames) {
		int count = 0;
		for (int i = 0; i < DataTest.size(); i++) {
			if (predictRe(root, DataTest.get(i), VarNames).equals(DataTest.get(i).get(DataTest.get(i).size()-1)))
				count++;
		}
		Double accuracy = Double.valueOf((double) count / DataTest.size());
		return accuracy;
	}
	

	
	public static String predictRe(DecisionTreeNode root, ArrayList<String> data, ArrayList<String> VarNames) {
		String s = "";
		while (!root.getBranch().isEmpty()) {
			//get var name
			int count = 0;
			for (int i = 0; i < VarNames.size(); i++) {
				if (root.getNode().equals(VarNames.get(i))) {
					count = i;
					break;
				}
			}
			for (int i = 0; i < root.getBranch().size(); i++) {
				//category
				if (!root.getBranch().get(0).substring(0,1).equals("<")) {
					if (data.get(count).equals(root.getBranch().get(i))) {
						root = root.getChildren().get(i);
						break;
					}
				}
				//numeric
				else {
					Double datas = Double.valueOf(data.get(count));
					Double thre = 0.0;
					if (i == 0) {
						thre = Double.valueOf(root.getBranch().get(i).substring(2,root.getBranch().get(i).length()));
						if (datas <= thre) {
							root = root.getChildren().get(i);
							break;
						}
					}
					else {
						thre = Double.valueOf(root.getBranch().get(i).substring(1,root.getBranch().get(i).length()));
						if (datas > thre) {
							root = root.getChildren().get(i);
							break;
						}
					}
				}
			}
		}
		s = root.getNode();
		return s;
	}
	
	public static DecisionTreeNode maketree2(ArrayList<ArrayList<String>> Data, DecisionTreeNode parent, ArrayList<ArrayList<String>> Levels, ArrayList<String> VarNames, Double m, String l) {
		ArrayList<ArrayList<String>> branch = BestSplit(Data, Levels, VarNames);
		if (Data.get(0).size() < m || Data.size() == 0 || branch.size() == 0 || oneCa(Data)) {
			DecisionTreeNode root = new DecisionTreeNode(l, parent);
			if (parent.getNode() != null) {
				parent.addChildren(root);
				root.updataParent(parent);
			}
			return root;
		}

		DecisionTreeNode root = new DecisionTreeNode(branch.get(0).get(0), parent);
		
		for (int i =0; i < branch.get(3).size()/2; i++) {
			if (branch.get(1).get(0).equals("=")) {
				root.addBranch(branch.get(2).get(i));
			}
			else {
				String string = branch.get(1).get(i);
				string += branch.get(2).get(0);
				root.addBranch(string);
			}
		}
		
		if (parent.getNode() != null) {
			parent.addChildren(root);
			root.updataParent(parent);
		}
			
		//traverse all nodes from the branch
		//if category
		for (int i = 0; i < branch.get(3).size()/2; i++) {
			if (branch.get(1).get(0).equals("=")) {
				ArrayList<ArrayList<String>>newData = SplitCaData(Data, branch.get(2).get(i), branch.get(0).get(0), VarNames, Levels);
				ArrayList<ArrayList<String>>newLel = SplitCaLel(Data, branch.get(2).get(i), branch.get(0).get(0), VarNames, Levels);
				ArrayList<String>newVar = SplitCaVar(Data, branch.get(2).get(i), branch.get(0).get(0), VarNames, Levels);
				int c1 = Integer.parseInt(branch.get(3).get(2*i));
				int c2 = Integer.parseInt(branch.get(3).get(2*i+1));
				if (c1 > c2) {
					l = "negative";
					root.updatenp(l);
				}
				if (c1 < c2) {
					l = "positive";
					root.updatenp(l);
				}
				if (c1 == c2){
					if (root.getParent() != null) {
						if (!root.getParent().getnp().equals(null)) {
							l = root.getParent().getnp();
							root.updatenp(root.getParent().getnp());
						}
					}
					else {
						l = "negative";
						root.updatenp(l);
					}
				}
				maketree2(newData, root, newLel, newVar, m, l);
			}
			//if numeric
			else {
				int c1 = Integer.parseInt(branch.get(3).get(2*i));
				int c2 = Integer.parseInt(branch.get(3).get(2*i+1));
				if (c1 > c2) {
					l = "negative";
					root.updatenp(l);
				}
				if (c1 < c2) {
					l = "positive";
					root.updatenp(l);
				}
				if (c1 == c2){
					if (root.getParent() != null) {
						if (!root.getParent().getnp().equals(null)) {
							l = root.getParent().getnp();
							root.updatenp(root.getParent().getnp());
						}
					}
					else {
						l = "negative";
						root.updatenp(l);
					}
				}	
				ArrayList<ArrayList<String>>newData = SplitNuData(Data, branch.get(2).get(0), i, branch.get(0).get(0), VarNames, Levels);
				maketree2(newData, root, Levels, VarNames, m, l);
			}
		}
		return root;
	}
	
	
	
	public static DecisionTreeNode maketree(ArrayList<ArrayList<String>> Data, DecisionTreeNode parent, ArrayList<ArrayList<String>> Levels, ArrayList<String> VarNames, Double m, String l) {
		ArrayList<ArrayList<String>> branch = BestSplit(Data, Levels, VarNames);
		if (Data.get(0).size() < m || Data.size() == 0 || branch.size() == 0 || oneCa(Data)) {
			DecisionTreeNode root = new DecisionTreeNode(l, parent);
			if (parent.getNode() != null) {
				parent.addChildren(root);
				root.updataParent(parent);
			}
			String ss = ":  ";
			ss += root.getNode();
			System.out.print(ss);
			return root;
		}

		DecisionTreeNode root = new DecisionTreeNode(branch.get(0).get(0), parent);
		
		for (int i =0; i < branch.get(3).size()/2; i++) {
			if (branch.get(1).get(0).equals("=")) {
				root.addBranch(branch.get(2).get(i));
			}
			else {
				String string = branch.get(1).get(i);
				string += branch.get(2).get(0);
				root.addBranch(string);
			}
		}
		
		if (parent.getNode() != null) {
			parent.addChildren(root);
			root.updataParent(parent);
		}
			
		//traverse all nodes from the branch
		//if category
		for (int i = 0; i < branch.get(3).size()/2; i++) {
			if (branch.get(1).get(0).equals("=")) {
				//print branch
				String s = "\n";
				for (int t = 1; t < levelTree(root); t ++) {
					s += "|";
					s += '\t';
				}
				s += branch.get(0).get(0);
				s += " ";
				s += branch.get(1).get(0);
				s += " ";
				s += branch.get(2).get(i);
				s += " [";
				s += branch.get(3).get(2*i);
				s += " ";
				s += branch.get(3).get(2*i+1);
				s += "]";
				System.out.print(s);
				ArrayList<ArrayList<String>>newData = SplitCaData(Data, branch.get(2).get(i), branch.get(0).get(0), VarNames, Levels);
				ArrayList<ArrayList<String>>newLel = SplitCaLel(Data, branch.get(2).get(i), branch.get(0).get(0), VarNames, Levels);
				ArrayList<String>newVar = SplitCaVar(Data, branch.get(2).get(i), branch.get(0).get(0), VarNames, Levels);
				int c1 = Integer.parseInt(branch.get(3).get(2*i));
				int c2 = Integer.parseInt(branch.get(3).get(2*i+1));
				if (c1 > c2) {
					l = "negative";
					root.updatenp(l);
				}
				if (c1 < c2) {
					l = "positive";
					root.updatenp(l);
				}
				if (c1 == c2){
					if (root.getParent() != null) {
						if (!root.getParent().getnp().equals(null)) {
							l = root.getParent().getnp();
							root.updatenp(root.getParent().getnp());
						}
					}
					else {
						l = "negative";
						root.updatenp(l);
					}
				}
				maketree(newData, root, newLel, newVar, m, l);
			}
			//if numeric
			else {
				//print branch
				String s = "\n";
				for (int t = 1; t < levelTree(root); t ++) {
					s += "|";
					s += '\t';
				}
				s += branch.get(0).get(0);
				s += " ";
				s += branch.get(1).get(i);
				s += " ";
				s += branch.get(2).get(0);
				s += " [";
				s += branch.get(3).get(2*i);
				s += " ";
				s += branch.get(3).get(2*i+1);
				s += "]";
				System.out.print(s);
				int c1 = Integer.parseInt(branch.get(3).get(2*i));
				int c2 = Integer.parseInt(branch.get(3).get(2*i+1));
				if (c1 > c2) {
					l = "negative";
					root.updatenp(l);
				}
				if (c1 < c2) {
					l = "positive";
					root.updatenp(l);
				}
				if (c1 == c2){
					if (root.getParent() != null) {
						if (!root.getParent().getnp().equals(null)) {
							l = root.getParent().getnp();
							root.updatenp(root.getParent().getnp());
						}
					}
					else {
						l = "negative";
						root.updatenp(l);
					}
				}	
				ArrayList<ArrayList<String>>newData = SplitNuData(Data, branch.get(2).get(0), i, branch.get(0).get(0), VarNames, Levels);
				maketree(newData, root, Levels, VarNames, m, l);
			}
		}
		return root;
	}
	
	
	public static int levelTree(DecisionTreeNode t) {
		int i = 1;
		while (t.getParent() != null) {
			t = t.getParent();
			i++;
		}
//		System.out.println("level: " + i);
		return i;
	}
	


	public static boolean oneCa(ArrayList<ArrayList<String>> Data) {
		int count = 0;
		if (Data.size() == 0)
			return true;
		for (int i = 0; i < Data.size(); i++) {
			if (count != 0) {
				break;
			}
			if (Data.get(i).size() == 0 || Data.get(i).size() == 1 )
				return true;
			else {
				for (int j =0; j < Data.get(i).size()-1; j++) {
					if (!Data.get(i).get(j).equals(Data.get(i).get(j+1))) {
						count ++;
						break;
					}
				}
			}
		}
		
		if (count == 0) {
			return true;
		}
		else return false;
	}

	// Run this subroutine for each numeric feature at each node of DT induction
	public static ArrayList<ArrayList<String>> SplitCaData(ArrayList<ArrayList<String>> Data, String Level, String VarName, ArrayList<String> VarNames, ArrayList<ArrayList<String>> Levels) {
		//删除一个var以后 levels、varnames也要对应删
		int pos = 0;
		ArrayList<Integer> newrow = new ArrayList<Integer>();
		ArrayList<ArrayList<String>> newDa = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < VarNames.size(); i++) {
			if (VarName.equals(VarNames.get(i))) {
				pos = i;
				break;
			}
		}
		for (int i = 0; i < Data.get(pos).size(); i++) {
			if (Data.get(pos).get(i).equals(Level)) {
				newrow.add(i);
			}
		}
		
		for (int i = 0; i < Data.size() - 1; i++) {
			newDa.add(new ArrayList<String>());
		}
		
		for (int i = 0; i < Data.size(); i++) {
			for (int j = 0; j < newrow.size(); j++) {
				if (i < pos) {
					newDa.get(i).add(Data.get(i).get(newrow.get(j)));
				}
				if (i > pos) {
					newDa.get(i-1).add(Data.get(i).get(newrow.get(j)));
				}
			}
		}
		
		return newDa;
	}
	
	public static ArrayList<String> SplitCaVar(ArrayList<ArrayList<String>> Data, String Level, String VarName, ArrayList<String> VarNames, ArrayList<ArrayList<String>> Levels) {
		//删除一个var以后 levels、varnames也要对应删
		int pos = 0;
		ArrayList<String> newVar = new ArrayList<String> ();
		for (int i = 0; i < VarNames.size(); i++) {
			if (VarName.equals(VarNames.get(i))) {
				pos = i;
				break;
			}
		}
		
		for (int i = 0; i < Data.size(); i++) {
			if ( i != pos) 
				newVar.add(VarNames.get(i));
		}
		
		return newVar;
	}
	
	public static ArrayList<ArrayList<String>> SplitCaLel(ArrayList<ArrayList<String>> Data, String Level, String VarName, ArrayList<String> VarNames, ArrayList<ArrayList<String>> Levels) {
		//删除一个var以后 levels、varnames也要对应删
		int pos = 0;
		ArrayList<ArrayList<String>> newLel = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < VarNames.size(); i++) {
			if (VarName.equals(VarNames.get(i))) {
				pos = i;
				break;
			}
		}
		
		for (int i = 0; i < Data.size() - 1; i++) {
			newLel.add(new ArrayList<String>());
		}
	
		for (int i = 0; i < Data.size(); i++) {
			for (int j = 0; j < Levels.get(i).size(); j++) {
				if (i < pos) {
					newLel.get(i).add(Levels.get(i).get(j));
				}
				if (i > pos) {
					newLel.get(i-1).add(Levels.get(i).get(j));
				}
			}
		}

		return newLel;
	}
	

	public static ArrayList<ArrayList<String>> SplitNuData(ArrayList<ArrayList<String>> Data, String Level, int a, String VarName, ArrayList<String> VarNames, ArrayList<ArrayList<String>> Levels) {
		//删除一个var以后 levels、varnames也要对应删
		int pos = 0;
		ArrayList<Integer> newrow = new ArrayList<Integer>();
		ArrayList<Double> only = new ArrayList<Double>();
		ArrayList<ArrayList<String>> newDa = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < VarNames.size(); i++) {
			if (VarName.equals(VarNames.get(i))) {
				pos = i;
				break;
			}
		}

		//transform string to double to get newrow
		for (int i = 0; i < Data.get(pos).size();i++) {
			only.add(Double.valueOf(Data.get(pos).get(i)));
		}
	
		//less than or equal
		if (a == 0) {
			for (int i = 0; i < Data.get(pos).size(); i++) {
				if (only.get(i) <= Double.valueOf(Level)) {
					newrow.add(i);
				}
			}
		}
		else {
			for (int i = 0; i < Data.get(pos).size(); i++) {
				if (only.get(i) > Double.valueOf(Level)) {
					newrow.add(i);
				}
			}
		}
		
		for (int i = 0; i < Data.size(); i++) {
			newDa.add(new ArrayList<String>());
		}
		
		for (int i = 0; i < Data.size(); i++) {
			for (int j = 0; j < newrow.size(); j++) {
				newDa.get(i).add(Data.get(i).get(newrow.get(j)));
			}
		}
		
		return newDa;
	}
	
	
	/*
	 *return the position of the node in varibles. 
	 */
	public static ArrayList<ArrayList<String>> BestSplit(ArrayList<ArrayList<String>> DataO, ArrayList<ArrayList<String>> Levels, ArrayList<String> VarNames) {
		//set a list of branches (include the positions) to return
		if (DataO.size() == 0) {
			return new ArrayList<ArrayList<String>>();
		}
		if (oneCa(DataO))
			return new ArrayList<ArrayList<String>>();
		int cc = 0;
		for (int i = 0; i < (DataO.get(DataO.size()-1).size())-1; i++) {
			if (!DataO.get(DataO.size()-1).get(i).equals(DataO.get(DataO.size()-1).get(i+1))) {
				cc++;
				break;
			}
		}
		if (cc == 0)
			return new ArrayList<ArrayList<String>>();
		
		ArrayList<ArrayList<String>> branch = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 4; i ++) {
			branch.add(new ArrayList<String>());
		}
		//set a list to store all e for better compare
		ArrayList<Double> e = new ArrayList<Double>();
		//set a list to store median thresold for each numeric var
		ArrayList<Double> allMedian = new ArrayList<Double>();
		//set a list to store the positions for numeric var
		ArrayList<Integer> position = new ArrayList<Integer>();
		Double eh = new Double(0);
		double p = 0;
		int sum = 0;
		//tier1, just positive or negative
		//if positive count/calculate prob/calculate e
		sum = count(DataO.get(DataO.size()-1), "positive");
		p = (double) sum / DataO.get(0).size();
		if (p != 0)
			eh -= p*(Math.log(p)/Math.log(2));
		//if negative count/calculate prob/sum up to e
		sum = count(DataO.get(DataO.size()-1), "negative");
		p = (double) sum/DataO.get(0).size();
		if (p != 0)
			eh -= p*(Math.log(p)/Math.log(2));
		
		
		//tier2, each var
		for (int i = 0; i < DataO.size()-1; i++) {
			Double t = new Double(eh);
			//if category
			if (!Levels.get(i).get(0).equals("real")) {
				//for each level
				for (int j = 0; j < Levels.get(i).size(); j++) {
					//if positive count/calculate prob/calculate e
					sum = count1(DataO, i, Levels.get(i).get(j), "positive");
					if (sum != 0)
						p = (double) sum/count(DataO.get(i), Levels.get(i).get(j));
					else p = 0;
					double p1 = 0;
					if (DataO.get(0).size() != 0 && count(DataO.get(i), Levels.get(i).get(j)) != 0)	
						p1 = (double) count(DataO.get(i), Levels.get(i).get(j)) / DataO.get(0).size();				
					if (p != 0)
						t += p1*p*(Math.log(p)/Math.log(2));
					//if negative count/calculate prob/sum up to e
					sum = count1(DataO, i, Levels.get(i).get(j), "negative");
					if (sum != 0)
						p = (double) sum/count(DataO.get(i), Levels.get(i).get(j));
					else p = 0;
					if (p != 0)
						t += p1*p*(Math.log(p)/Math.log(2));
				}
				t = (double)Math.round(t * 1000000000) / 1000000000;
				e.add(t);
			}
			//if numeric
			else {
				//get medians
				ArrayList<Double> medians = Medians(DataO.get(i));
				ArrayList<Double> es = new ArrayList<Double>();
				if (!medians.isEmpty()) {
					for (int j = 0; j < medians.size(); j++) {
						Double ess = new Double(eh);
						//less or equal to median
						//positive
						ArrayList<Double> d = splitPNNU(DataO, i, "positive");
						//negative
						ArrayList<Double> d1 = splitPNNU(DataO, i, "negative");
						//if positive
						if (countLS(d, medians.get(j)) != 0)
							p = (double) countLS(d, medians.get(j))/(countLS(d, medians.get(j))+countLS(d1, medians.get(j)));
						else p = 0;
						double p1 = 0;
						if (countLS(d, medians.get(j))+countLS(d1, medians.get(j)) != 0 && DataO.get(0).size() != 0)
							p1 = (double) (countLS(d, medians.get(j))+countLS(d1, medians.get(j)))/DataO.get(0).size();
						if (p != 0)
							ess += p1*p*(Math.log(p)/Math.log(2));
						//if negative
						if (countLS(d1, medians.get(j)) != 0)
							p = (double) countLS(d1, medians.get(j))/(countLS(d, medians.get(j))+countLS(d1, medians.get(j)));
						else p = 0;
						if (p != 0)
							ess += p1*p*(Math.log(p)/Math.log(2));
						//greater than median
						//if positive
						if (countG(d, medians.get(j)) != 0)
							p = (double) countG(d, medians.get(j))/(countG(d, medians.get(j))+countG(d1, medians.get(j)));
						else p = 0;
						if (countG(d, medians.get(j)) + countG(d, medians.get(j)) != 0 && DataO.get(0).size() != 0)
							p1 = (double) (countG(d, medians.get(j))+countG(d1, medians.get(j)))/DataO.get(0).size();
						else p1 = 0;
						if (p != 0)
							ess += p1*p*(Math.log(p)/Math.log(2));
						//if negative
						if (countG(d1, medians.get(j)) != 0)
							p = (double) countG(d1, medians.get(j))/(countG(d, medians.get(j))+countG(d1, medians.get(j)));
						else p = 0;
						if (p != 0)
							ess += p1*p*(Math.log(p)/Math.log(2));
						es.add(ess);
					}
				}
				t = max(es);
				int index = maxIndex(es);
				if (! medians.isEmpty()) {
					Double median = medians.get(index);
					allMedian.add(median);
					position.add(i);
					t = (double)Math.round(t * 1000000000) / 1000000000;
					e.add(t);
				}
				else 
					e.add(t);
			}
		}
		
		int a = 0;
		//if max is less than 0, return new ArrayList
		if (max(e) <= 0) 
			return new ArrayList<ArrayList<String>>();
		//use maxIndex function to get the index of the largest var in e
		else {
			a = maxIndex(e);
//			System.out.println();
//			System.out.println("maxIdx: " + a);
		} 
		//create branch function (node name, symbols, levels, count
		branch.get(0).add(VarNames.get(a));
		//categorical
		if (!Levels.get(a).get(0).equals("real")) {
			branch.get(1).add("=");
			//add levels
			for (int i = 0; i < Levels.get(a).size(); i++) {
				branch.get(2).add(Levels.get(a).get(i));
			}
			//add nums
			for (int i = 0; i < Levels.get(a).size(); i++) {
				//if positive count/calculate prob/calculate e
				sum = count1(DataO, a, Levels.get(a).get(i), "negative");
				branch.get(3).add(Integer.toString(sum));
				//if negative count/calculate prob/sum up to e
				sum = count1(DataO, a, Levels.get(a).get(i), "positive");
				branch.get(3).add(Integer.toString(sum));
			}	
		}
		//numeric
		else {
			branch.get(1).add("<=");
			branch.get(1).add(">");
			//add levels
			Double median = new Double(0);
			for (int i = 0; i < position.size(); i++) {
				if (a == position.get(i)) {
					median = allMedian.get(i);
					double round = (double) Math.round(median.doubleValue()*1000000)/1000000;
					branch.get(2).add(String.format("%.6f", round));
					break;
				}
			}
			//add nums
			ArrayList<Double> d = splitPNNU(DataO, a, "negative");
			ArrayList<Double> d1 = splitPNNU(DataO, a, "positive");
			//if no more than median
			branch.get(3).add(Integer.toString(countLS(d, median)));
			branch.get(3).add(Integer.toString(countLS(d1, median)));
			branch.get(3).add(Integer.toString(countG(d, median)));
			branch.get(3).add(Integer.toString(countG(d1, median)));
		}
		//return branch list
		return branch;
	}
	
	//helper funtion for bestsplit. The main purpose is to find the position of max e
	public static int maxIndex(ArrayList<Double> num) {
		int a = 0;
		if (!num.isEmpty()) {
			for (int i = 0; i < num.size(); i++) {
				if (num.get(i) == (max(num))) {
					a = i;
					return a;
				}
			}
		}
		
		return a;
	}
	
	public static Double max(ArrayList<Double> num) {
		if (!num.isEmpty()) {
			Double a = Double.MIN_VALUE;
			for (int i = 0; i < num.size(); i++) {
				if (num.get(i) > a) {
					a = num.get(i);
				}
			}
			return a;
		}
		return (double) 0.00;
	}
	
	//2 helper funtion for bestsplit. The main purpose is to get the sum of a level.
	public static int count(ArrayList<String> a, String b) {
		int t = 0;
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).equals(b)) {
				t++;
			}
		}
		return t;
	}
	
	public static int count1(ArrayList<ArrayList<String>> Data, int t, String a, String b ) {
		int s = 0;
		for (int i = 0; i < Data.get(t).size(); i++) {
			String x = Data.get(t).get(i);
			boolean T = x.equals(a);
			String y = Data.get(Data.size()-1).get(i);
			boolean z = y.equals(b);
			boolean both = T&&z;
			if (both) {
				s++;
			}
		}
		return s;
	}
	
	//helper function for bestsplit. The main purpose is to get get positive part or negative part of the data
	public static ArrayList<String> splitPN(ArrayList<ArrayList<String>> Data, int t, String s) {
		ArrayList<String> a = new ArrayList<String> ();
		for (int i = 0; i < Data.get(0).size(); i++) {
			if (Data.get(Data.size()-1).get(i).equals(s)) {
				a.add(Data.get(t).get(i));
			}
		}
		return a;
	}
	
	public static ArrayList<Double> splitPNNU(ArrayList<ArrayList<String>> Data, int t, String s) {
		ArrayList<Double> a = new ArrayList<Double> ();
		for (int i = 0; i < Data.get(0).size(); i++) {
			if (Data.get(Data.size()-1).get(i).equals(s)) {
				a.add(Double.parseDouble(Data.get(t).get(i)));
			}
		}
		return a;
	}
	
	//helper function to calculate median
	    
	public static ArrayList<Double> Medians(ArrayList<String> m) {
		HashMap<Integer, String> unique = new HashMap<Integer, String>();
		ArrayList<Double> u1 = new ArrayList<Double>();
		ArrayList<Double> u2 = new ArrayList<Double>();
		if (m.size() < 2)
			return new ArrayList<Double>();
		int j = 0;
		for (int i = 0; i < m.size(); i++) {
			if (!unique.containsValue(m.get(i))) {
				unique.put(j, m.get(i));
				j++;
			}
		}
		for (int i = 0; i <j; i++) {
			u1.add(Double.valueOf(unique.get(i)));
		}
		Collections.sort(u1);
		for (int i = 0; i < u1.size()-1; i++) {
			double sum = u1.get(i)+u1.get(i+1);
			double ave = sum/2;
			u2.add(ave);
		}
		return u2;
	}
	   

	    

	
	//helper function to count greater or no more than median
	public static int countLS(ArrayList<Double> num, Double median) {
		int a = 0;
		for (int i = 0; i < num.size(); i++) {
			if (num.get(i) < median) {
				a++;
			}
		}
		return a;
	}
	
	public static int countG(ArrayList<Double> num, Double median) {
		int a = 0;
		for (int i = 0; i < num.size(); i++) {
			if (num.get(i) > median) {
				a++;
			}
		}
		return a;
	}
}




