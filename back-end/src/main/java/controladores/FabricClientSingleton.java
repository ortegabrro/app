package controladores;

import client.ChannelClient;
import client.FabricClient;


public class FabricClientSingleton {
	private FabricClient fabricClient;
	private ChannelClient channelClient;
	private static FabricClientSingleton myConfig = null;

	// Private constructor suppresses
	private FabricClientSingleton(FabricClient userContext,ChannelClient channelClient) {
		this.fabricClient=userContext;
		this.channelClient=channelClient;
	}


	private synchronized static void createInstance(FabricClient userContext,ChannelClient channelClient) {
		if (myConfig == null) {
			myConfig = new FabricClientSingleton(userContext,channelClient);
		}
	}

	public static FabricClientSingleton getInstance(FabricClient userContext,ChannelClient channelClient) {
		if (myConfig == null) {
			createInstance(userContext,channelClient);
		}
		return myConfig;
	}
	
	public static FabricClientSingleton getInstance() {		
		return myConfig;
	}


	public FabricClient getFabricClient() {
		return fabricClient;
	}


	public void setFabricClient(FabricClient fabricClient) {
		this.fabricClient = fabricClient;
	}


	public ChannelClient getChannelClient() {
		return channelClient;
	}


	public void setChannelClient(ChannelClient channelClient) {
		this.channelClient = channelClient;
	}
	
	
}
