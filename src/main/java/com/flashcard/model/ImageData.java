package com.flashcard.model;


import com.flashcard.model.enums.CardFace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ImageData")
@Table(name = "image_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] data;

    @Enumerated(EnumType.STRING)
    private CardFace face;


    public ImageData getFrontData() {
        if (this.getFace().equals(CardFace.FRONT)) {
            return this;
        }
        return null;
    }

    public ImageData getBackData() {
        if (this.getFace().equals(CardFace.BACK)) {
            return this;
        }
        return null;
    }
}
