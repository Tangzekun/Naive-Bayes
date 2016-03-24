import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.*;;

import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;


class nb 
{
	static int num_Instance = 0;
	static int test_Num_Instance =0;
	
	public static void main(String[] args) throws IOException 
	{
		String trainingFile = args[0];
		String testingFile = args[1];
		String betaInput = args[2];
		String modelFile = args[3];
		
		
		int positiveValueCount =0;
		int negativeValueCount =0;
		
		double positiveProbility =0.0;
		double negativeProbility =0.0;
		


		String [] attributeName = null;
		String [] testAttributeName = null;
		int rowLength =0;
		int testRowLength = 0;
		ArrayList<int[]> fileContent = new ArrayList <int[]> ();
		ArrayList<int[]> testFileContent = new ArrayList <int[]> ();
		BufferedReader trainReader = null;
		BufferedReader testReader = null;
		
        try
        {
            String line = "";
            String nameLine = "";
            String testNameLine ="";
            trainReader = new BufferedReader(new FileReader(trainingFile)); 
            testReader = new BufferedReader(new FileReader(testingFile)); 
            
            
            nameLine = trainReader.readLine();
    		attributeName = nameLine.split(",");
    		rowLength = attributeName.length;
            while ((line = trainReader.readLine()) != null) 
            {	            	
            	String [] rowContentStr ;          	
            	rowContentStr = line.split(",");  
            	int[] rowContent = new int[rowContentStr.length];
            	for(int i = 0;i < rowContentStr.length;i++)
            	{
            		rowContent[i] = Integer.parseInt(rowContentStr[i]);
            	}
            	fileContent.add(rowContent);
            	num_Instance = fileContent.size();
            }
//			System.out.println("Num_Instance : " + num_Instance);


       
           testNameLine = testReader.readLine();
           testAttributeName = testNameLine.split(",");
           testRowLength = testAttributeName.length;
           while ((line = testReader.readLine()) != null) 
           {
            	String [] rowContentStr ;          	
            	rowContentStr = line.split(",");  
            	int[] testRowContent = new int[rowContentStr.length];
            	for(int i = 0;i < rowContentStr.length;i++)
            	{
            		testRowContent[i] = Integer.parseInt(rowContentStr[i]);
            	}
            	testFileContent.add(testRowContent);
            	test_Num_Instance = testFileContent.size();
            }
//            System.out.println("test_Num_Instance : " + num_Instance);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally
        {
            try 
            {
                trainReader.close();
//                testReader.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }


	
	for (int i =0; i< num_Instance; i++)
	{	
		int outputValue = 0;
		outputValue= fileContent.get(i)[rowLength-1];
		if (outputValue == 1)
		{
			positiveValueCount +=1;
		}
		else
		{
			negativeValueCount +=1;
		}
	}
	
//	System.out.println("Positive Value Count " + positiveValueCount);
//	System.out.println("Negative Value Count " + negativeValueCount);
	positiveProbility = (float) positiveValueCount / num_Instance;
	negativeProbility = (float) negativeValueCount / num_Instance;
	
	
	
	
	ArrayList<MyPair> ratioContent = new ArrayList <MyPair> ();
	for (int j =0; j < rowLength-1; j++)
	{	
		int attributeValue = 0;
		int outputValue = 0;
		int attributePositivePositiveCount =0;
		int attributePositiveNegativeCount =0;
		int attributeNegativePositiveCount =0;
		int attributeNegativeNegativeCount =0;
		
		double attributePositivePositiveRatio =0.0;
		double attributePositiveNegativeRatio =0.0;
		double attributeNegativePositiveRatio =0.0;
		double attributeNegativeNegativeRatio =0.0;
		

		for (int i =0; i< num_Instance; i++)
		{
			attributeValue = fileContent.get(i)[j];
			outputValue = fileContent.get(i)[rowLength -1];
			
			if(attributeValue ==1 && outputValue ==1 )
			{
				attributePositivePositiveCount++;
			}
			else if(attributeValue ==1 && outputValue ==0)
			{
				attributePositiveNegativeCount++;
			}
			else if(attributeValue ==0 && outputValue ==1)
			{
				attributeNegativePositiveCount++;
			}
			else if(attributeValue ==0 && outputValue ==0)
			{
				attributeNegativeNegativeCount++;
			}	
		}
//		System.out.println(positiveValueCount);
//		System.out.println(negativeValueCount);
		
		attributePositivePositiveRatio = (float) attributePositivePositiveCount / positiveValueCount;
		attributePositiveNegativeRatio = (float) attributePositiveNegativeCount / negativeValueCount;
		attributeNegativePositiveRatio = (float) attributeNegativePositiveCount / positiveValueCount;
		attributeNegativeNegativeRatio = (float) attributeNegativeNegativeCount / negativeValueCount;

//		System.out.println();
		
//		System.out.println("Attribute  " + j + "  PositivePositiveRatio : "  + attributePositivePositiveRatio);
//		System.out.println("Attribute  " + j + "  PositiveNegativeRatio : "  + attributePositiveNegativeRatio);
//		System.out.println("Attribute  " + j + "  NegativePositiveRatio : "  + attributeNegativePositiveRatio);
//		System.out.println("Attribute  " + j + "  NegativeNegativeRatio : "  + attributeNegativeNegativeRatio);
//		System.out.println();
		
		
		
		
		MyPair pair = new MyPair (attributePositivePositiveRatio, attributePositiveNegativeRatio, attributeNegativePositiveRatio, attributeNegativeNegativeRatio);
		ratioContent.add(pair);
		
	}
	

	int counterCorrect = 0;
	double accuracy = 0;
	for (int i =0; i< test_Num_Instance; i++)
	{	
		int testAttributeValue = 0;
		double predictionPositive = 0.0;
		double predictionNegative = 0.0;
		int predictionFinal =0;
		int testAttributeOutput =0;
		
		for (int j =0; j< testRowLength-1; j++)
		{	
			
			testAttributeValue = testFileContent.get(i)[j]; 
			if (testAttributeValue == 1)
			{
				predictionPositive += ratioContent.get(j).attributePPRatio();
				predictionNegative += ratioContent.get(j).attributePNRatio() ;
			}
			else 
			{
				predictionPositive += ratioContent.get(j).attributeNPRatio();
				predictionNegative += ratioContent.get(j).attributeNNRatio();
			}
		}
		
		
		if(predictionPositive >= predictionNegative)
		{
			predictionFinal =1;
		}
		else 
		{
			predictionFinal = 0;	
		}
		
		
		testAttributeOutput = testFileContent.get(i)[testRowLength-1];
		if (testAttributeOutput == predictionFinal)
		{
			counterCorrect ++;
		}
		
//		System.out.println ("PredictionPositive  " + predictionPositive);
//		System.out.println ("PredictionNegative  " + predictionNegative);
//		System.out.println ("Prediction Value: " + predictionFinal);
//		System.out.println ("Actual Value: " + testAttributeOutput);
//		System.out.println();
		
	}
	
		accuracy = (float) counterCorrect / test_Num_Instance;
//		System.out.println("Accuracy : " + accuracy);
		
		double biase = 0;
		double biaseSum =0;
		double weight =0; 
		 		
		int testOutputValue =0;
		int testAttributeNum =0;
		
		int positiveOutputCounter =0;
		int negativeOutputCounter =0;
		
		int testAttributePositivePositiveCounter =0;
		int testAttributePositiveNegativeCounter =0;
		int testAttributeNegativePositiveCounter =0;
		int testAttributeNegativeNegativeCounter =0;
		
		double testAttributePositivePositiveRatio =0.0;
		double testAttributePositiveNegativeRatio =0.0;
		double testAttributeNegativePositiveRatio =0.0;
		double testAttributeNegativeNegativeRatio =0.0;
		
		ArrayList<Double> weightArray = new ArrayList <Double> ();
		ArrayList<MyPair> testRatioContent = new ArrayList <MyPair> ();
//		ArrayList<int> testAttribute = 
		
	
		for (int i =0; i< test_Num_Instance; i++)
		{	
			testOutputValue = testFileContent.get(i)[testRowLength-1];
			if(testOutputValue == 1)
			{
				positiveOutputCounter++;
			}
			else
			{
				negativeOutputCounter++;
			}
		}
		
		
		for (int j =0; j< testRowLength-1; j++)
		{	
			for (int i =0; i< test_Num_Instance; i++)
			{	
				testAttributeNum = testFileContent.get(i)[j];
				testOutputValue = testFileContent.get(i)[testRowLength-1];
				
				if (testAttributeNum == 1 && testOutputValue ==1)
				{
					testAttributePositivePositiveCounter++;
				}
				else if(testAttributeNum == 0 && testOutputValue ==1) 
				{
					testAttributeNegativePositiveCounter++;
				}
				else if(testAttributeNum == 0 && testOutputValue ==1) 
				{
					testAttributeNegativePositiveCounter++;
				}
				else if(testAttributeNum == 0 && testOutputValue ==1) 
				{
					testAttributeNegativeNegativeCounter++;
				}

				
			}
			testAttributePositivePositiveRatio = (float) testAttributePositivePositiveCounter / positiveValueCount;
			testAttributePositiveNegativeRatio = (float) testAttributePositiveNegativeCounter / negativeValueCount;
			testAttributeNegativePositiveRatio = (float) testAttributeNegativePositiveCounter / positiveValueCount;
			testAttributeNegativeNegativeRatio = (float) testAttributeNegativeNegativeCounter / negativeValueCount;
			
			MyPair testPair = new MyPair (testAttributePositivePositiveRatio, testAttributePositiveNegativeRatio, 										testAttributeNegativePositiveRatio, testAttributeNegativeNegativeRatio);
			testRatioContent.add(testPair);
		}
		
		double ratioNPOverNN =0;
		double ratioPPOverPN =0;
		for(int j=0; j< testRowLength-1; j++)
		{	
			ratioNPOverNN = testRatioContent.get(j).attributeNPRatio() / ratioContent.get(j).attributeNNRatio();
			biaseSum += Math.log(ratioNPOverNN);
		}
//		System.out.println("positiveOutputCounter: " + positiveOutputCounter);
//		System.out.println("negativeOutputCounter:  "+ negativeOutputCounter);
		biase = Math.log(positiveOutputCounter / negativeOutputCounter) + biaseSum;
		writeFile(modelFile, "", biase);
		
		
		for(int j=0; j< testRowLength-1; j++)
		{
			ratioNPOverNN = testRatioContent.get(j).attributeNPRatio() / ratioContent.get(j).attributeNNRatio();
			ratioPPOverPN = testRatioContent.get(j).attributePPRatio() / ratioContent.get(j).attributePNRatio();
			weight = ratioPPOverPN - ratioNPOverNN;
//			weightArray.add(weight);
			writeFile(modelFile, testAttributeName[j] +"  ", weight);
		}
	}
	
	
	
	
	public static void writeFile(String model, String attribute, double corresponding) throws IOException
	{
		File fout = new File(model);
		if(!fout.exists())
		{
			fout.createNewFile();
		}
		
		FileWriter fileWritter = new FileWriter(fout.getName(),true);
	    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);

	    bufferWritter.write(attribute+" "+ corresponding);
	    bufferWritter.newLine();
	    bufferWritter.close();
	}

}




class MyPair
{
    private final Double attributePPRatio; // pp
    private final Double attributePNRatio; // pn
	private final Double attributeNPRatio; // np
	private final Double attributeNNRatio; // nn

    public MyPair(Double ratio1, Double ratio2, Double ratio3 , Double ratio4)
    {
		
		attributePPRatio = ratio1;
		attributePNRatio = ratio2;
		attributeNPRatio = ratio3;
		attributeNNRatio = ratio4;
		
    }

    public Double attributePPRatio() { return attributePPRatio; }
    public Double attributePNRatio() { return attributePNRatio; }
    public Double attributeNPRatio() { return attributeNPRatio; }
    public Double attributeNNRatio() { return attributeNNRatio; }

}











