package it.gennaro.unisa.utils;

import it.gennaro.unisa.Exception.BlockChainException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class GameUtil {
    private static final BigInteger GAS_LIMIT = new BigInteger("200000");
    private static final BigInteger GAS_PRICE = Convert.toWei("5", Convert.Unit.GWEI).toBigInteger();
    private static final ContractGasProvider contractGasProvider = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);
    private final Web3j web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
    private final Credentials c;
    private final Game gameEngine;
    private final String gameAddr;


    public GameUtil(String gameAddr, String userPKey) {
        this.gameAddr = gameAddr;
        this.c = Credentials.create(userPKey);
        TransactionManager tm = new RawTransactionManager(web3j, c, 200, 500);
        gameEngine = Game.load(gameAddr, web3j, tm, contractGasProvider);
    }

    public void payForGold(double value) throws BlockChainException {
        try{
            BigInteger nonce = web3j.ethGetTransactionCount(c.getAddress(), DefaultBlockParameterName.PENDING).send().getTransactionCount();
            Transaction tx = new Transaction(c.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, gameAddr, BigInteger.valueOf((long) (Long.parseLong("1000000000000000000") * value)), "");
            web3j.ethSendTransaction(tx).send();
        }catch (Exception e ){
            System.err.println(e.getMessage());
            throw new BlockChainException("payForGold()");
        }
    }

    public BigInteger getGold() throws BlockChainException {
        try {
            return gameEngine.getGold().send();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("getGold()");
        }
    }
    public String getOwner(long id, long num) throws BlockChainException {
        try {
            return gameEngine.getOwner(BigInteger.valueOf(id), BigInteger.valueOf(num)).send();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("getOwner()");
        }
    }
    public List<Item> getArmory() throws BlockChainException {
        try {
            LinkedList<Item> list = new LinkedList<>();
            int max = gameEngine.getArmoryWeight().send().intValue();
            for (int i = 0; i < max; i++) {
                Tuple3<String, BigInteger, BigInteger> ris = gameEngine.getItemFromArmory(BigInteger.valueOf(i)).send();
                list.add(new Item(ris.component1(), ris.component2(), ris.component3()));
            }
            return list;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("getArmory()");
        }
    }
    public List<Item> getInventory() throws BlockChainException {
        try {
            LinkedList<Item> list = new LinkedList<>();
            int max = gameEngine.getInventoryWeight().send().intValue();
            for (int i = 0; i < max; i++) {
                Tuple3<String, BigInteger, BigInteger> ris = gameEngine.getItemFromInvenory(BigInteger.valueOf(i)).send();
                list.add(new Item(ris.component1(), ris.component2(), ris.component3()));
            }
            return list;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("getInventory()");
        }
    }

    public void buy(long id) throws BlockChainException {
        try {
            gameEngine.buyItem(BigInteger.valueOf(id)).send();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("buy()");
        }
    }
    public void sell(long id) throws BlockChainException {
        try {
            gameEngine.sellItem(BigInteger.valueOf(id)).send();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("sell()");
        }
    }


    //Admin Function
    public void createItem(String name, long price, long qtyMAX) throws BlockChainException {
        try {
            gameEngine.createItem(name, BigInteger.valueOf(price), BigInteger.valueOf(qtyMAX)).send();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("createItem()");
        }
    }
    public void updatePlayer(String add, long gold) throws BlockChainException {
        try {
            gameEngine.updatePlayerInfo(add, BigInteger.valueOf(gold)).send();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("updatePlayer()");
        }
    }
    public void drawBack() throws BlockChainException {
        try {
            gameEngine.drawback().send();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new BlockChainException("drawBack()");
        }
    }

    public void close() {
        web3j.shutdown();
    }
}
