package com.jpmc.midascore;




import com.jpmc.midascore.entity.Incentive;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;


@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${general.kafka-topic}")
    private String topic;

    @KafkaListener(topics="${general.kafka-topic}")
    public void listen(Transaction transaction){

        String url="http://localhost:8080/incentive";
        UserRecord sender=userRepository
                .findById(transaction.getSenderId())
                .orElseThrow(()->new RuntimeException("Sender not found"));
        UserRecord recipient=userRepository
                .findById(transaction.getRecipientId())
                .orElseThrow(()->new RuntimeException("Recipient not found"));
        float amount=transaction.getAmount();

        if(sender.getBalance()<amount)
            return;
        //adjust balance
        Incentive incentive=restTemplate.postForObject(url,transaction,Incentive.class);
        float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0;

        sender.setBalance(sender.getBalance()-amount);


        recipient.setBalance(recipient.getBalance()+amount+incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        //record transaction
        TransactionRecord record=new TransactionRecord();
        record.setSender(sender);
        record.setRecipient(recipient);
        record.setAmount(amount);
        transactionRecordRepository.save(record);


    }
}


