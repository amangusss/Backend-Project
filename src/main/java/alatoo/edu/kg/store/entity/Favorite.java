//package alatoo.edu.kg.store.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "favorites")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class Favorite {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    Entry entry;
//}
