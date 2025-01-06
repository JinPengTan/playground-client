package my.paynet.jposclient.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.MUX;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller {
    private static final int[] fields = {2,3,4,7,11,12,13,14,15,17,18,22,25,32,37,38,39,44,49,60,61,66,125,126,128};
    private final MUX mux;
    ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();
    AtomicInteger counter = new AtomicInteger(0);

    public Controller(MUX mux) {
        this.mux = mux;
    }


    public String randomNumeric(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be non-negative");
        }

        return new Random().ints(length, 0, 10)
            .mapToObj(Integer::toString)
            .collect(Collectors.joining());
    }

    @GetMapping("/preauth")
    public String preAuth() {
        ISOMsg isoMsg = new ISOMsg();
        try {
            isoMsg.setMTI("0100");
            isoMsg.set(2, "5265580000000208");
            isoMsg.set(3, "000000");
            isoMsg.set(4, "000000100000");
            isoMsg.set(7, "0905140730");
            isoMsg.set(11, "140730");
            isoMsg.set(12, "140730");
            isoMsg.set(13, "0905");
            isoMsg.set(14, "2409");
            isoMsg.set(17, "0905");
            isoMsg.set(18, "4111");
            isoMsg.set(22, "010");
            isoMsg.set(25, "56");
            isoMsg.set(27, "6");
            isoMsg.set(32, "60334600000");
            isoMsg.set(121, randomNumeric(20));
            isoMsg.set(37, "250020040111");
            isoMsg.set(42, "501100001120001");
            isoMsg.set(43, "PAYNET CNP/T31 TESTING   KUALA LUMPURMY ");
            isoMsg.set(44, "051679");
            isoMsg.set(48,
                "PAYNET DS TESTING1 ~        ~!4317FDC3AD245443800000000000089100000000~!16~!010201000000001B308BEA38D3E3FB48F7EF0101~   ~ ~            ~      ~0000".getBytes());
            isoMsg.set(49, "458");
            isoMsg.set(60, "HLB UAT2+0000000");
            isoMsg.set(61, "BKRBUAT200000000000");
            isoMsg.set(66, "1");
            isoMsg.set(125, "P BICIB24 10");
            isoMsg.set(126, "20300011120276100000000000000000000000");
            isoMsg.set(128, ISOUtil.hex2byte("32A6E84B00000000"));

            ISOMsg response = mux.request(isoMsg, 45000);

            if (response == null) {
                throw new RuntimeException("Response is null");
            }

            LinkedHashMap<Integer, String> respMap = new LinkedHashMap<>();
            respMap.put(0, response.getMTI());
            for (int field : fields) {
                respMap.put(field, response.getString(field));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(respMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @GetMapping("/preauth-load")
    public String preAuthLoad() {
        ISOMsg isoMsg = new ISOMsg();
        try {
            isoMsg.setMTI("0100");
            isoMsg.set(2, "5265580000000208");
            isoMsg.set(3, "000000");
            isoMsg.set(4, "000000100000");
            isoMsg.set(7, "0905140730");
            isoMsg.set(11, "140730");
            isoMsg.set(12, "140730");
            isoMsg.set(13, "0905");
            isoMsg.set(14, "2409");
            isoMsg.set(17, "0905");
            isoMsg.set(18, "4111");
            isoMsg.set(22, "010");
            isoMsg.set(25, "56");
            isoMsg.set(27, "6");
            isoMsg.set(32, "60334600000");
            isoMsg.set(121, randomNumeric(20));
            isoMsg.set(37, "250020040111");
            isoMsg.set(42, "501100001120001");
            isoMsg.set(43, "PAYNET CNP/T31 TESTING   KUALA LUMPURMY ");
            isoMsg.set(44, "051679");
            isoMsg.set(48,
                    "PAYNET DS TESTING1 ~        ~!4317FDC3AD245443800000000000089100000000~!16~!010201000000001B308BEA38D3E3FB48F7EF0101~   ~ ~            ~      ~0000".getBytes());
            isoMsg.set(49, "458");
            isoMsg.set(60, "HLB UAT2+0000000");
            isoMsg.set(61, "BKRBUAT200000000000");
            isoMsg.set(66, "1");
            isoMsg.set(125, "P BICIB24 10");
            isoMsg.set(126, "20300011120276100000000000000000000000");
            isoMsg.set(128, ISOUtil.hex2byte("32A6E84B00000000"));

            while(counter.get() < 5000000) {
                log.info("Counter : {}", counter.incrementAndGet());
                service.submit(() -> {
//                    log.info("START Thread");
                    try {
                        ISOMsg res = mux.request(isoMsg, 45000);
                        if (res == null) {
                            throw new RuntimeException("Response is null");
                        }
                    } catch (ISOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter.set(0);
        return "ok";
    }

    @GetMapping("p66")
    public String p66Issue() {
        ISOMsg isoMsg = new ISOMsg();
        try {
            isoMsg.setMTI("0100");
            isoMsg.set(2, "5265580000000208");
            isoMsg.set(3, "000000");
            isoMsg.set(4, "000000100000");
            isoMsg.set(7, "0905140730");
            isoMsg.set(11, "140730");
            isoMsg.set(12, "140730");
            isoMsg.set(13, "0905");
            isoMsg.set(14, "2409");
            isoMsg.set(17, "0905");
            isoMsg.set(18, "4111");
            isoMsg.set(22, "010");
            isoMsg.set(25, "56");
            isoMsg.set(27, "6");
            isoMsg.set(32, "60334600000");
            isoMsg.set(121, randomNumeric(20));
            isoMsg.set(37, "250020040111");
            isoMsg.set(42, "501100001120001");
            isoMsg.set(43, "PAYNET CNP/T31 TESTING   KUALA LUMPURMY ");
            isoMsg.set(44, "051679");
            isoMsg.set(48,
                    "PAYNET DS TESTING1 ~        ~!4317FDC3AD245443800000000000089100000000~!16~!010201000000001B308BEA38D3E3FB48F7EF0101~   ~ ~            ~      ~0000".getBytes());
            isoMsg.set(49, "458");
            isoMsg.set(60, "HLB UAT2+0000000");
            isoMsg.set(61, "BKRBUAT200000000000");
            isoMsg.set(66, "1");
            isoMsg.set(125, "P BICIB24 10");
            isoMsg.set(126, "20300011120276100000000000000000000000");
            isoMsg.set(128, ISOUtil.hex2byte("32A6E84B00000000"));

            ISOMsg response = mux.request(isoMsg, 45000);

            if (response == null) {
                throw new RuntimeException("Response is null");
            }

            LinkedHashMap<Integer, String> respMap = new LinkedHashMap<>();
            respMap.put(0, response.getMTI());
            for (int field : fields) {
                respMap.put(field, response.getString(field));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(respMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

}
