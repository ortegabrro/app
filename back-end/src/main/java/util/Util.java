/****************************************************** 
 *  Copyright 2018 IBM Corporation 
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License.
 */

package util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import user.CAEnrollment;
import user.UserContext;

import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import config.Config;
import controladores.FabricClientSingleton;

/**
 * 
 * @author Balaji Kadambi
 *
 */

public class Util {
	
	private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);

	private static final String EXPECTED_EVENT_NAME = "event";


	private static  Map<String, byte[]> getTransientMap() {
		Map<String, byte[]> tm2 = new HashMap<>();
		tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
		tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
		tm2.put("result", ":)".getBytes(UTF_8));
		tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
		return tm2;
	}

	public static Collection<ProposalResponse> createInvoke(String function, String[] arguments) {
		Collection<ProposalResponse> responses = null;
		try {
			TransactionProposalRequest request = FabricClientSingleton.getInstance().getFabricClient().getInstance()
					.newTransactionProposalRequest();
			request.setChaincodeID(Config.ccid);
			request.setFcn(function);
			request.setArgs(arguments);
			request.setProposalWaitTime(2000);
			request.setTransientMap(getTransientMap());
			responses = FabricClientSingleton.getInstance().getChannelClient().sendTransactionProposal(request);
		} catch (Exception e) {
			System.out.println("Error creating invoke");
		}
		return responses;
	}
	
	public static Collection<ProposalResponse> createQuery(String function, String[] arguments) {
		Collection<ProposalResponse> responses = null;
		try {
			responses=FabricClientSingleton.getInstance().getChannelClient().queryByChainCode(Config.CHAINCODE_1_NAME, function, arguments);
		} catch (Exception e) {
			System.out.println("Error creating query");
		}
		return responses; 
	}

	/**
	 * Serialize user
	 * 
	 * @param userContext
	 * @throws Exception
	 */
	public static void writeUserContext(UserContext userContext) throws Exception {
		String directoryPath = "users/" + userContext.getAffiliation();
		String filePath = directoryPath + "/" + userContext.getName() + ".ser";
		File directory = new File(directoryPath);
		if (!directory.exists())
			directory.mkdirs();

		FileOutputStream file = new FileOutputStream(filePath);
		ObjectOutputStream out = new ObjectOutputStream(file);

		// Method for serialization of object
		out.writeObject(userContext);

		out.close();
		file.close();
	}

	/**
	 * Deserialize user
	 * 
	 * @param affiliation
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public static UserContext readUserContext(String affiliation, String username) throws Exception {
		String filePath = "users/" + affiliation + "/" + username + ".ser";
		File file = new File(filePath);
		if (file.exists()) {
			// Reading the object from a file
			FileInputStream fileStream = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(fileStream);

			// Method for deserialization of object
			UserContext uContext = (UserContext) in.readObject();

			in.close();
			fileStream.close();
			return uContext;
		}

		return null;
	}

	/**
	 * Create enrollment from key and certificate files.
	 * 
	 * @param folderPath
	 * @param keyFileName
	 * @param certFileName
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws CryptoException
	 */
	public static CAEnrollment getEnrollment(String keyFolderPath, String keyFileName, String certFolderPath,
			String certFileName)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, CryptoException {
		PrivateKey key = null;
		String certificate = null;
		InputStream isKey = null;
		BufferedReader brKey = null;

		try {

			isKey = new FileInputStream(keyFolderPath + File.separator + keyFileName);
			brKey = new BufferedReader(new InputStreamReader(isKey));
			StringBuilder keyBuilder = new StringBuilder();

			for (String line = brKey.readLine(); line != null; line = brKey.readLine()) {
				if (line.indexOf("PRIVATE") == -1) {
					keyBuilder.append(line);
				}
			}

			certificate = new String(Files.readAllBytes(Paths.get(certFolderPath, certFileName)));

			byte[] encoded = DatatypeConverter.parseBase64Binary(keyBuilder.toString());
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
			KeyFactory kf = KeyFactory.getInstance("EC");
			key = kf.generatePrivate(keySpec);
		} finally {
			isKey.close();
			brKey.close();
		}

		CAEnrollment enrollment = new CAEnrollment(key, certificate);
		return enrollment;
	}

	public static void cleanUp() {
		String directoryPath = "users";
		File directory = new File(directoryPath);
		deleteDirectory(directory);
	}

	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(children[i]);
				if (!success) {
					return false;
				}
			}
		}

		// either file or an empty directory
		Logger.getLogger(Util.class.getName()).log(Level.INFO, "Deleting - " + dir.getName());
		return dir.delete();
	}

}
