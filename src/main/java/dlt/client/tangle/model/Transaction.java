package dlt.client.tangle.model;

import dlt.client.tangle.enums.TransactionType;

/**
 *
 * @author Uellington Damasceno
 */
public class Transaction {
	private String source;
    private TransactionType type;
    private double avgLoad;
    private double lastLoad;
    private boolean lbEntry;
    private String group;
    private String target;
    private long timestamp;
    private String deviceSwapId;
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public double getAvgLoad() {
		return avgLoad;
	}
	public void setAvgLoad(double avgLoad) {
		this.avgLoad = avgLoad;
	}
	public double getLastLoad() {
		return lastLoad;
	}
	public void setLastLoad(double lastLoad) {
		this.lastLoad = lastLoad;
	}
	public boolean getLbEntry() {
		return lbEntry;
	}
	public void setLbEntry(boolean lbEntry) {
		this.lbEntry = lbEntry;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDeviceSwapId() {
		return deviceSwapId;
	}
	public void setDeviceSwapId(String deviceSwapId) {
		this.deviceSwapId = deviceSwapId;
	}
	
	




}

