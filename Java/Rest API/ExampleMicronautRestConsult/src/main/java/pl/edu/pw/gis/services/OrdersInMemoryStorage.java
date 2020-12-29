package pl.edu.pw.gis.services;

import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpResponse;
import pl.edu.pw.gis.dto.Order;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Primary
@Singleton
public class OrdersInMemoryStorage implements OrdersStorage {
    private final List<Order> orders;
    private long counter = 0;

    public OrdersInMemoryStorage() {
        Order l1 = new Order(counter++,  21.02,51.01, "Aneta", "Bocianowska", "Mickiewicza 17 Warszawa", "Poland", "2839101444", true, 1.5);
        Order l2 = new Order(counter++,  19.02,52.01, "Jakub", "Kosterna", "Warszawska 50 Gdańsk", "Poland", "5492049253", false, 10);
        Order l3 = new Order(counter++,  3.166,46.01, "Patryk", "Wrona", "Shakalaka 50", "France", "431634753", false, 60);
        Order l4 = new Order(counter++,  7.0926,31.7917, "Hakim", "Mansouri", "Bumbum 35", "Algeria", "123456789", false, 1000);
        Order l5 = new Order(counter++,  46.63,-19.38, "Mia", "Rakotomalala", "Bumbum 35", "Madagascar", "987654321", true, 250);
        Order l6 = new Order(counter++,  77,22, "Ramesh", "Agarwal", "AAaAAAAaa 44", "India", "1212121212", true, 2);
        Order l7 = new Order(counter++, 10, 53.6, "Sophia", "Müller", "Schmetterling 666", "Germany", "665544332", false, 12);
        Order l8 = new Order(counter++,-4.038216, 50.648058 , "Theresa", "May", "London Street 55", "England", "845555322", true, 120 );
        Order l9 = new Order(counter++,46.4825, 30.7233 , "Vitaly", "Grachey", "Odessa 54", "Ukraine", "46735245", true, 1 );
        Order l10 = new Order(counter++,-66.5897, 6.4238,"Alejandra", "Rodriguez", "Rampampampam 123", "Venezuela", "123321123", false, 1111 );
        Order l11 = new Order(counter++,-51.9253, -14.2350 , "Fernanda", "Sousa", "Ugabuga 33", "Brazil", "696969696", false, 3 );
        Order l12 = new Order(counter++,-106.3468, 56.1304 , "Logan", "Tremblay", "Toronto 55%", "Canada", "1122334455", true, 7 );
        Order l13 = new Order(counter++,34.8888, 6.3690,  "Baraka", "Juma", "Tanzania 54", "Tanzania", "13523432", false, 3 );
        Order l14 = new Order(counter++,80.7718, 7.8731,  "Lanka", "Perera", "Batticaloa 1", "Sri Lanka", "84684576562", false, 14 );
        Order l15 = new Order(counter++,94.9120, 27.4728, "Bhārata", "Reddy", "Dibrugarh 643a", "Brazil", "696969696", false, 3 );
        Order l16 = new Order(counter++,120.9842, 14.5995 , "Chesa", "Gonzales", "Manila 64c", "Philippines", "7345234", true, 2.5 );
        Order l17 = new Order(counter++,133.7751, -25.2744 , "Lucas", "Brown", "Canberra 98421", "Australia", "987654321", true, 1116 );
        Order l18 = new Order(counter++,1.0789, 51.2802 , "Emily", "Taylor", "Canterbury 34n/4", "England", "112233225", false, 0.3 );
        Order l19 = new Order(counter++,66.9237, 48.0196 , "Sanzhar", "Alieva", "Viuuu 54d", "Kazakhstan", "24623456", true, 1 );
        Order l20 = new Order(counter++,124.1238, 66.7613 , "Anastasia", "Ivanova", "Russian 123", "Russia", "412353623", false, 4.5 );
        Order l21 = new Order(counter++,104.1954, 35.8617, "Li Wei", "Huang", "Hong Kong 534", "China", "69696969669", false, 1133.333 );
        Order l22 = new Order(counter++,138.2529, 36.2048, "Ouga", "Takahashi", "Koenji Junjo 64", "Japan", "1643454", true, 0.03);
        Order l23 = new Order(counter++, 38.9637, 35.2433 , "Defne", "Çelik", "Eminonu Square 44", "Turkey", "1342142454", false, 1144 );
        Order l24 = new Order(counter++,84.1240, 28.3949 , "Bengali", "Shrestha", "Anchalpur 3a", "Nepal", "723414246", true, 13 );
        Order l25 = new Order(counter++, 12.5674, 41.8719 , "Giuseppe", "Romano", "Duparomana 52d", "Italy", "361353321", false, 8.2);
        Order l26 = new Order(counter++,69.3451, 30.3753 , "Ghufran", "Farooqi", "M. A. Jinnah Road 94f", "Pakistan", "222266664", false, 1163.234 );
        Order l27 = new Order(counter++,116.4074, 39.9042 , "Wang Fang", "Xu", "Wangfujing Street 678", "China", "4123214345", false, 1111 );
        Order l28 = new Order(counter++,53.6880, 32.4279 , "Farhad", "Mohammadi", "Ferdowsi Street Fff", "Iran", "888234324", true, 4.20 );
        Order l29 = new Order(counter++,127.7669, 35.9078 , "Ha-yoon", "Park", "Gangnam shopping street 12345", "South Korea", "74262435210", true, 3.1415 );
        Order l30 = new Order(counter++,127.5101, 40.3399 , "Kim", "Jong-un", "<UNKNOWN>", "North Korea", "420420420", false, 1234);
        Order l31 = new Order(counter++,95.7129, 37.0902 , "Jennifer", "Miller", "Wall Street 23", "United States", "76245457456", true, 221);
        Order l32 = new Order(counter++,-63.6167, -38.4161 , "Xavier", "Gomez", "Córdoba Street 432", "Argentina", "515123532", true, 290);
        Order l33 = new Order(counter++,-42.6043, 71.7069 , "Malu", "Jensen", "Aqqusinersuaq 34b", "Greenland", "3131365", false, 292.4);
        Order l34 = new Order(counter++,-75.6972, 45.4215 , "Ivy", "Martin", "Ottawa 13c", "Canada", "3112332112", true, 23);
        Order l35 = new Order(counter++,-98.8139, 53.7609 , "Leah", "MacDonald", "Manitoba ^_^", "Canada", "3135437768", false, 1.8);
        Order l36 = new Order(counter++,37.6476, 53.7267 , "Alexander", "Kuznetsov", "Lenin Street 717", "Russia", "891324083", false, 0.03);
        Order l37 = new Order(counter++,8.4689, 60.4720 , "Alf", "Johansen", "Geiranger 2mb", "Norway", "46583234", true, 222);
        Order l38 = new Order(counter++,18.6435, 60.1282 , "Olivia", "Karlsson", "Strandvägen 360c", "Sweden", "342656543", false, 39.5);
        Order l39 = new Order(counter++,-90.8139, 53.7609 , "Emma", "Gagnon", "Ellesmere Island 4gD235$", "Canada", "75423561174", true, 20);
        Order l40 = new Order(counter++,0.8248, 8.6195 , "Joseph", "Akakpo", "Dapaong 233", "Togo", "52542341", false, 448);

        orders = new ArrayList<>();
        orders.add(l1);
        orders.add(l2);
        orders.add(l3);
        orders.add(l4);
        orders.add(l5);
        orders.add(l6);
        orders.add(l7);
        orders.add(l8);
        orders.add(l9);
        orders.add(l10);
        orders.add(l11);
        orders.add(l12);
        orders.add(l13);
        orders.add(l14);
        orders.add(l15);
        orders.add(l16);
        orders.add(l17);
        orders.add(l18);
        orders.add(l19);
        orders.add(l20);
        orders.add(l21);
        orders.add(l22);
        orders.add(l23);
        orders.add(l24);
        orders.add(l25);
        orders.add(l26);
        orders.add(l27);
        orders.add(l28);
        orders.add(l29);
        orders.add(l30);
        orders.add(l31);
        orders.add(l32);
        orders.add(l33);
        orders.add(l34);
        orders.add(l35);
        orders.add(l36);
        orders.add(l37);
        orders.add(l38);
        orders.add(l39);
        orders.add(l40);
    }

    @Override
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public boolean addOrder(Order order) {
        order.setId(++counter);
        return orders.add( order );
    }

    @Override
    public boolean deleteOrder(int orderId) {
        // orders.remove( orderId );

        if(orders.get( orderId ) != null) {
            orders.remove( orderId );
            return true;
        }
        return false;
    }

    @Override
    public boolean putOrder(int orderId, Order order){
        if (orders.get( orderId ) != null) {
            orders.get( orderId ).setName( order.getName() );
            orders.get( orderId ).setSurname( order.getSurname() );
            orders.get( orderId ).setLng( order.getLng() );
            orders.get( orderId ).setLat( order.getLat() );
            orders.get( orderId ).setAddress( order.getAddress() );
            orders.get( orderId ).setCountry( order.getCountry() );
            orders.get(orderId).setContactNumber( order.getContactNumber() );
            orders.get(orderId).setPickup( order.isPickup() );
            orders.get(orderId).setWeight( order.getWeight() );
            return true;
        }
        else return false;
    }

}
