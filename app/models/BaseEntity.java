package models;

import java.sql.Timestamp;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;


@MappedSuperclass
abstract public class BaseEntity extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	
	@CreatedTimestamp
	public Timestamp createdOn;
	
	@Version
	public Timestamp lastUpdate;
	
}
