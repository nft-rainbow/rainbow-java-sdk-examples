package com.yiidian;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.model.*;
import org.openapitools.client.model.ServicesContractDeployDto.TypeEnum;
import org.openapitools.client.api.LoginApi;
import org.openapitools.client.api.ContractApi;
import org.openapitools.client.api.MintsApi;
import org.openapitools.client.api.TransfersApi;
import org.openapitools.client.api.FilesApi;
import org.openapitools.client.api.MetadataApi;

import java.io.File;
import java.util.List;

public class App 
{
    public static void main(String[] args) throws Exception{
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://api.nftrainbow.cn/v1");

        login(defaultClient);

    }

    /******************************* Login to get token ********************************/
    public static void login(ApiClient defaultClient) throws Exception{
        LoginApi apiInstance = new LoginApi(defaultClient);
        MiddlewaresAppLogin appLoginInfo = new MiddlewaresAppLogin(); // MiddlewaresAppLogin | login info, contain app_id and app_secret
        appLoginInfo.appId("BQYVOA5n");
        appLoginInfo.appSecret("P9U5WUv81oBPjjPS");
        try {
            MiddlewaresLoginResp result = apiInstance.loginApp(appLoginInfo);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling LoginApi#loginApp");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /************** Upload file to get fileurl **************/
    public static void uploadFile(ApiClient defaultClient, String token){
        FilesApi apiInstance = new FilesApi(defaultClient);
        String authorization = "Bearer " + token; // String | Bearer openapi_token
        File _file = new File("/path/to/file"); // File | uploaded file
        try {
            ServicesUploadFilesResponse result = apiInstance.uploadFile(authorization, _file);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling FilesApi#uploadFile");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /************** Create metadata to get metadata uri according to the fileURL from `uploadFile()` **************/

    public static void createMetadata(ApiClient defaultClient, 
    String token, 
    String fileURL, 
    String name, 
    String description,
    List<ModelsExposedMetadataAttribute> attributes
    ) {
        MetadataApi apiInstance = new MetadataApi(defaultClient);
        String authorization = "Bearer " + token; // String | Bearer openapi_token
        ServicesMetadataDto metadataInfo = new ServicesMetadataDto(); // ServicesMetadataDto | metadata_info
        metadataInfo.setImage(fileURL);
        metadataInfo.setName(authorization);
        metadataInfo.setDescription(description);
        metadataInfo.setName(name);
        metadataInfo.attributes(attributes);

        try {
            ModelsExposedMetadata result = apiInstance.createMetadata(authorization, metadataInfo);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MetadataApi#createMetadata");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /************** deploy erc732 or erc1155 contract **************/
    public static void deployContract(ApiClient defaultClient, String token) throws Exception{
        ContractApi apiInstance = new ContractApi(defaultClient);
        
        String authorization = "Bearer " + token; // String | Bearer Open_JWT **token is from login**
        ServicesContractDeployDto contractInfo = new ServicesContractDeployDto(); // ServicesContractDeployDto | contract_info
        contractInfo.setName("TEST");
        contractInfo.setChain(org.openapitools.client.model.ServicesContractDeployDto.ChainEnum.CONFLUX_TEST);
        contractInfo.setSymbol("1231");
        contractInfo.setOwnerAddress("cfxtest:aar9up0wsbgtw7f0g5tyc4hbwb2wa5wf7emmk94znd");
        contractInfo.setType(TypeEnum.ERC721);
        contractInfo.setTokensBurnable(true);
        contractInfo.setTokensTransferableByAdmin(true);
        contractInfo.setTokensTransferableByUser(true);
        contractInfo.setIsSponsorForAllUser(true);

        try {
            ModelsContract result = apiInstance.deployContract(authorization, contractInfo);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ContractApi#deployContract");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /************** get the contract address after deploying contract **************/

    //Note: Id is from `result.getId()` from `deployContract()`
    public static void getContract(ApiClient defaultClient, String token, Integer id) throws Exception{
        ContractApi apiInstance = new ContractApi(defaultClient);
        
        String authorization = "Bearer " + token; // String | Bearer Open_JWT
        ServicesContractDeployDto contractInfo = new ServicesContractDeployDto(); // ServicesContractDeployDto | contract_info
        try {
            ModelsContract result = apiInstance.getContractInfo(authorization, id);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ContractApi#getContractInfo");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /************** set the sponsor for the  deployed contract **************/

    // address is the from `result.getAddress()` from `getContract()`
    public static void setSponsor(ApiClient defaultClient, String token, String address)throws Exception{
        ContractApi apiInstance = new ContractApi(defaultClient);
        String authorization = "Bearer " + token; // String | Bearer Open_JWT
        try {
            ServicesSetSponsorResp result = apiInstance.setContractSponsor(authorization, address);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ContractApi#setContractSponsor");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /************** mint NFT according to the deployed contract **************/

    // metadataURI is from `createMetadata()`'s response'
    // contract is from `getContract()`'s response'
    // metadataURI is from `createMetadata()'s response'`
    public static void customMint(ApiClient defaultClient, String token, String contract, String mintTo, String metadataURI)throws Exception{
        MintsApi apiInstance = new MintsApi(defaultClient);
        String authorization = "Bearer " + token; // String | Bearer Open_JWT
        ServicesCustomMintDto customMintDto = new ServicesCustomMintDto(); // ServicesCustomMintDto | custom_mint_dto
        customMintDto.setChain(org.openapitools.client.model.ServicesCustomMintDto.ChainEnum.CONFLUX_TEST);
        customMintDto.setAmount(1);
        customMintDto.setContractAddress(contract);
        customMintDto.setMintToAddress(mintTo);
        customMintDto.setMetadataUri(metadataURI);

        try {
            ModelsMintTask result = apiInstance.customMint(authorization, customMintDto);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MintsApi#customMint");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /************** mint NFT according to the deployed contract **************/
    // id is from `customMint()`

    public static void getMintDetail(ApiClient defaultClient, String token, Integer id) {
        MintsApi apiInstance = new MintsApi(defaultClient);
        String authorization = "Bearer " + token; // String | Bearer Open_JWT
        try {
            ModelsMintTask result = apiInstance.getMintDetail(authorization, id);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MintsApi#getMintDetail");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /************** tranfer NFT **************/

    // tokenId is from `customMint()`'s response'
    public static void transferNFT(ApiClient defaultClient, String token, String contract, String to, String from, String tokenId)throws Exception{
        TransfersApi apiInstance = new TransfersApi(defaultClient);
        String authorization = "Bearer " + token; // String | Bearer Open_JWT
        ServicesTransferDto transfersDto = new ServicesTransferDto(); // ServicesCustomMintDto | custom_mint_dto
        transfersDto.setChain(org.openapitools.client.model.ServicesTransferDto.ChainEnum.CONFLUX_TEST);
        transfersDto.setAmount(1);
        transfersDto.setContractAddress(contract);
        transfersDto.setTransferFromAddress(from);
        transfersDto.setTransferToAddress(to);
        transfersDto.setTokenId(tokenId);

        try {
            ModelsTransferTask result = apiInstance.transferNft(authorization, transfersDto);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling TransfersApi#transferNft");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
