package call_chain_to_filter_map;

import call_chain_to_filter_map.converter.CallChainToFilterMap;
import org.junit.Assert;
import org.junit.Test;
import call_chain_to_filter_map.parser.ArrayOperations;
import call_chain_to_filter_map.parser.exceptions.ArrayOperationsException;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class CallChainToFilterMapTest {
    private void valid(final String callChain, final String expected) {
        Assert.assertEquals(expected, toFilterMap(callChain));
    }

    private String toFilterMap(final String callChain) {
        return CallChainToFilterMap.convert(ArrayOperations.parse(callChain));
    }

    private void invalid(final String callChain) {
        try {
            String result = toFilterMap(callChain);
            Assert.fail("Expected fail, found " + result + " for " + callChain);
        } catch (ArrayOperationsException e) {
            System.out.println("Expected error");
            System.out.println("    " + callChain);
            System.out.println("    " + e.getMessage());
        }
    }

    @Test
    public void test1() {
        valid("filter{(element>10)}%>%filter{(element<20)}",
                "filter{((element>10)&(element<20))}%>%map{element}");
    }

    @Test
    public void test2() {
        valid("map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}",
                "filter{(element>0)}%>%map{(((element*element)+(20*element))+100)}");
    }

    @Test
    public void test3() {
        valid("filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}",
                "filter{(1=0)}%>%map{element}");
    }

    @Test
    public void test4() {
        valid("filter{((element*element)>1)}%>%map{((2*element)+(10*element))}%>%filter{(element<25)}%>%map{(element*element)}",
                "filter{((element<-1)|(element=2))}%>%map{((144*element)*element)}");
    }

    @Test
    public void test5() {
        valid("map{(element*element)}%>%filter{((element < 4)  | (element> -10))}",
                "filter{(0=0)}%>%map{(element*element)}");
    }

    @Test
    public void test6() {
        valid("map{(((element + (element * element)) * element)    - element)}",
                "filter{(0=0)}%>%map{((((element*element)*element)+(element*element))-element)}");
    }

    @Test
    public void test7() {
        valid("filter{((element > 1000) | (((element  = 0) |  (element =10)) | ((element < 0) & (element > -1))))}",
                "filter{(((element=0)|(element=10))|(element>1000))}%>%map{element}");
    }

    @Test
    public void test8() {
        valid("filter{ ((element > -1) &  (element < 0)) }     \t%>% map{(element + 10)}",
                "filter{(1=0)}%>%map{element}");
    }

    @Test
    public void test9() {
        valid("filter{ ((element > -1) &  (element < 0)) }     \t%>% map{(element + 10)}%>%filter{(((element * element) * element) > 3)}",
                "filter{(1=0)}%>%map{element}");
    }

    @Test
    public void test10() {
        valid("filter{((1 + ((element * element) + element)) > 0)}%>%map{(element + 100000)}",
                "filter{(0=0)}%>%map{(element+100000)}");
    }

    @Test
    public void test11() {
        invalid("");
    }

    @Test
    public void test12() {
        invalid("filter{}");
    }

    @Test
    public void test13() {
        invalid("map{(element * element) + 2)}");
    }

    @Test
    public void test14() {
        invalid("map{(element > 10)}");
    }

    @Test
    public void test15() {
        invalid("filter{(element + 2)}");
    }

    @Test
    public void test16() {
        invalid("filter{(((element * element) * element) < (element = 0))}");
    }

    @Test
    public void test17() {
        invalid("filter{(elem > 2)}%>%map{(elem * elem)}");
    }

    @Test
    public void test18() {
        valid("map{(element - -23)}",
                "filter{(0=0)}%>%map{(element+23)}");
    }

    @Test
    public void test19() {
        invalid("map{(element - -23e10)}");
    }

    @Test
    public void test20() {
        valid("map{13}%>%filter{(element > 21)}",
                "filter{(1=0)}%>%map{element}");
    }

    @Test
    public void test21() {
        valid("map{21}%>%filter{(element = 21)}",
                "filter{(0=0)}%>%map{21}");
    }

    @Test
    public void test22() {
        valid("map{21}%>%filter{(element > 20)}",
                "filter{(0=0)}%>%map{21}");
    }
}
