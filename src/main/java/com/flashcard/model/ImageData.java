package com.flashcard.model;


import com.flashcard.model.enums.CardFace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "ImageData")
@Table(name = "image_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE image_data SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ImageData {// card larda bulunan ön ve arka yüz resimlerini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] data;

    @Enumerated(EnumType.STRING)
    private CardFace face;

    private boolean deleted = false;
}
