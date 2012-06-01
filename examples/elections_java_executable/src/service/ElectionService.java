/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.15.0.1751 modeling language!*/

package service;
import java.util.List;
import java.util.ArrayList;
import shared.domain.Election;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import shared.Credentials;

public class ElectionService
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static ElectionService theInstance = null;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ElectionService Attributes
  private Election newElection;
  private List<Election> elections;
  private Connection theConnection;
  private boolean electionAdded;

  //ElectionService State Machines
  enum ElectionServiceCycle { Idle, LoadingAllElections, CreatingElection }
  private ElectionServiceCycle ElectionServiceCycle;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  private ElectionService()
  {
    setElectionServiceCycle(ElectionServiceCycle.Idle);
  }

  public static ElectionService getInstance()
  {
    if(theInstance == null)
    {
      theInstance = new ElectionService();
    }
    return theInstance;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNewElection(Election aNewElection)
  {
    boolean wasSet = false;
    newElection = aNewElection;
    wasSet = true;
    createElection();
    return wasSet;
  }

  public boolean setElections(List<Election> aElections)
  {
    boolean wasSet = false;
    elections = aElections;
    wasSet = true;
    return wasSet;
  }

  public boolean setElectionAdded(boolean aElectionAdded)
  {
    boolean wasSet = false;
    electionAdded = aElectionAdded;
    wasSet = true;
    return wasSet;
  }

  public Election getNewElection()
  {
    return newElection;
  }

  public List<Election> getElections()
  {
    return elections;
  }

  public boolean getElectionAdded()
  {
    return electionAdded;
  }

  public String getElectionServiceCycleFullName()
  {
    String answer = ElectionServiceCycle.toString();
    return answer;
  }

  public ElectionServiceCycle getElectionServiceCycle()
  {
    return ElectionServiceCycle;
  }

  public boolean getAllElections()
  {
    boolean wasEventProcessed = false;
    
    ElectionServiceCycle aElectionServiceCycle = ElectionServiceCycle;
    switch (aElectionServiceCycle)
    {
      case Idle:
        setElectionServiceCycle(ElectionServiceCycle.LoadingAllElections);
        wasEventProcessed = true;
        break;
    }

    return wasEventProcessed;
  }

  public boolean createElection()
  {
    boolean wasEventProcessed = false;
    
    ElectionServiceCycle aElectionServiceCycle = ElectionServiceCycle;
    switch (aElectionServiceCycle)
    {
      case Idle:
        setElectionServiceCycle(ElectionServiceCycle.CreatingElection);
        wasEventProcessed = true;
        break;
    }

    return wasEventProcessed;
  }

  private boolean __autotransition46__()
  {
    boolean wasEventProcessed = false;
    
    ElectionServiceCycle aElectionServiceCycle = ElectionServiceCycle;
    switch (aElectionServiceCycle)
    {
      case LoadingAllElections:
        setElectionServiceCycle(ElectionServiceCycle.Idle);
        wasEventProcessed = true;
        break;
    }

    return wasEventProcessed;
  }

  private boolean __autotransition47__()
  {
    boolean wasEventProcessed = false;
    
    ElectionServiceCycle aElectionServiceCycle = ElectionServiceCycle;
    switch (aElectionServiceCycle)
    {
      case CreatingElection:
        setElectionServiceCycle(ElectionServiceCycle.Idle);
        wasEventProcessed = true;
        break;
    }

    return wasEventProcessed;
  }

  private void setElectionServiceCycle(ElectionServiceCycle aElectionServiceCycle)
  {
    try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			theConnection = DriverManager.getConnection("jdbc:mysql://"+Credentials.getInstance().getDb_hostname()+"/elections", Credentials.getInstance().getDb_username(), Credentials.getInstance().getDb_password());
		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
    ElectionServiceCycle = aElectionServiceCycle;

    // entry actions and do activities
    switch(ElectionServiceCycle)
    {
      case LoadingAllElections:
        loadAllElections();
        __autotransition46__();
        break;
      case CreatingElection:
        addElection();
        __autotransition47__();
        break;
    }
  }

  public void delete()
  {}


  public void loadAllElections(){
      elections=new ArrayList<Election>();
		
		try {
			Statement stmt = theConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM election");
			while (rs.next()) {
				String name = rs.getString("name");
				String description = rs.getString("description");
				int id=Integer.parseInt(rs.getString("id_election"));
				Election election=new Election(id, name, description);
				elections.add(election);
			}
		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
  }


  public void addElection(){
      try {
			Statement stmt = theConnection.createStatement();
			stmt.executeUpdate("insert into elections.election (name, description) values ('"+newElection.getName()+"', '"+newElection.getDescription()+"')");
			electionAdded=true;
		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
			electionAdded=false;
		}
  }

}