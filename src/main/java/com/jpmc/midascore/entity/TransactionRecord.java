package com.jpmc.midascore.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue()
    private long transactionId;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name="recipient_id")
    private UserRecord recipient;

    private float amount;

     public TransactionRecord(){

     }

     public TransactionRecord(Long transactionId,UserRecord sender, UserRecord recipient,float amount){
         this.transactionId=transactionId;
         this.sender=sender;
         this.recipient =recipient;
         this.amount=amount;
     }

     public Long getTransactionId(){
         return transactionId;
     }
     public void setId(Long id){
         this.transactionId=id;
     }
     public float getAmount(){
         return amount;
     }
     public void setAmount(float amount1){
         this.amount=amount1;
     }
    public UserRecord getSender(){
         return sender;
    }
    public void setSender(UserRecord sender1){
         this.sender=sender1;
    }
    public UserRecord getRecipient(){
         return recipient;
    }
    public void setRecipient(UserRecord recipient1){
         this.recipient =recipient1;
    }

}
