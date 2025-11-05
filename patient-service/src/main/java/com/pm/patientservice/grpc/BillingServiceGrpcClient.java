package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}")  String serverAddress,
            @Value("${billing.service.grpc.port:9001}")       int serverPort
    ) {
        log.info("[CONNECTING TO BILLING SERVICE AT : ] {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccounts(String patientId, String Email, String name){
        BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId).setName(name).setEmail(Email).build();
        BillingResponse response = blockingStub.createBillingAccount(request);

        log.info("[RESPONSE] from billing service via grpc : [{}] ", response);

        return response;
    }
}
