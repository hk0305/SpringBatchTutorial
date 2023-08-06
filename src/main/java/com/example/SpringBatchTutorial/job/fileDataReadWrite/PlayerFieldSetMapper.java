package com.example.SpringBatchTutorial.job.fileDataReadWrite;

import com.example.SpringBatchTutorial.job.fileDataReadWrite.dto.Player;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class PlayerFieldSetMapper implements FieldSetMapper<Player> {

    /**
     * 읽어온 데이터 셋을 플레이어에 설정
     *
     * @param fieldSet the {@link FieldSet} to map
     * @return
     */
    public Player mapFieldSet(FieldSet fieldSet) {
        Player player = new Player();

        player.setID(fieldSet.readString(0));
        player.setLastName(fieldSet.readString(1));
        player.setFirstName(fieldSet.readString(2));
        player.setPosition(fieldSet.readString(3));
        player.setBirthYear(fieldSet.readInt(4));
        player.setDebutYear(fieldSet.readInt(5));

        return player;
    }
}
