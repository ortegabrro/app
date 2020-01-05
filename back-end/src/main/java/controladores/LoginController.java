package controladores;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;

import client.CAClient;
import client.ChannelClient;
import client.FabricClient;
import config.Config;
import user.UserContext;
import util.Util;

public class LoginController {
	public static void bootstrapLogin() {
		try {
        Util.cleanUp();
		String caUrl = Config.CA_ORG1_URL;
		CAClient caClient = new CAClient(caUrl, null);
		// Enroll Admin to Org1MSP
		UserContext adminUserContext = new UserContext();
		adminUserContext.setName(Config.ADMIN);
		adminUserContext.setAffiliation(Config.ORG1);
		adminUserContext.setMspId(Config.ORG1_MSP);
		caClient.setAdminUserContext(adminUserContext);
		adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);
		
		FabricClient fabClient = new FabricClient(adminUserContext);
		
		ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
		Channel channel = channelClient.getChannel();
		Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
		EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
		Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
		channel.addPeer(peer);
		channel.addEventHub(eventHub);
		channel.addOrderer(orderer);
		channel.initialize();
		FabricClientSingleton.getInstance(fabClient, channelClient);
		}catch (Exception e) {
			System.out.println("Connection problems");
		}
	}
}
