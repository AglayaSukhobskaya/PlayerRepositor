package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Data;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exceptions.ExceptionsNOT_FOUND;
import com.game.repository.PlayerRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerService {
    PlayerRepository playerRepository;

    public List<Player> getPlayerList(String name,
                                      String title,
                                      Race race,
                                      Profession profession,
                                      Long after,
                                      Long before,
                                      Boolean banned,
                                      Integer minExperience,
                                      Integer maxExperience,
                                      Integer minLevel,
                                      Integer maxLevel,
                                      PlayerOrder order,
                                      Integer pageNumber,
                                      Integer pageSize) {
        List<Player> players = (List<Player>) playerRepository.findAll();
        List<Player> playersList = new ArrayList<>();
        for (Player player : players) {
            if (name != null) if (!player.getName().contains(name)) continue;
            if (title != null) if (!player.getTitle().contains(title)) continue;
            if (race != null) if (!player.getRace().equals(race)) continue;
            if (profession != null) if (!player.getProfession().equals(profession)) continue;
            if (after != null) if (player.getBirthday().getTime() < after) continue;
            if (before != null) if (player.getBirthday().getTime() > before) continue;
            if (banned != null) if (!player.getBanned().equals(banned)) continue;
            if (minExperience != null) if (player.getExperience() < minExperience) continue;
            if (maxExperience != null) if (player.getExperience() > maxExperience) continue;
            if (minLevel != null) if (player.getLevel() < minLevel) continue;
            if (maxLevel != null) if (player.getLevel() > maxLevel) continue;
            playersList.add(player);
        }

        if (Objects.nonNull(order)) {
            switch (order) {
                case NAME: playersList.sort(Comparator.comparing(Player::getName));
                case EXPERIENCE: playersList.sort(Comparator.comparing(Player::getExperience));
                case BIRTHDAY: playersList.sort(Comparator.comparing(Player::getBirthday));
            }
        } else {
            playersList.sort(Comparator.comparing(Player::getId));
        }

        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;

        List<Player> smallList = new ArrayList<>();
        for (int i = pageNumber * pageSize; i < pageNumber * pageSize + pageSize && i < playersList.size(); i++) {
            smallList.add(playersList.get(i));
        }

        return smallList;
    }

    public Long getPlayersCount(String name, String title, Race race,
                                Profession profession, Long after, Long before,
                                Boolean banned, Integer minExperience, Integer maxExperience,
                                Integer minLevel, Integer maxLevel) {

        if (playerRepository.count() == 0) return 0L;

        Long count = 0L;
        for (Player player : playerRepository.findAll()) {
            if (name != null) if (!player.getName().contains(name)) continue;
            if (title != null) if (!player.getTitle().contains(title)) continue;
            if (race != null) if (!player.getRace().equals(race)) continue;
            if (profession != null) if (!player.getProfession().equals(profession)) continue;
            if (after != null) if (player.getBirthday().getTime() < after) continue;
            if (before != null) if (player.getBirthday().getTime() > before) continue;
            if (banned != null) if (!player.getBanned().equals(banned)) continue;
            if (minExperience != null) if (player.getExperience() < minExperience) continue;
            if (maxExperience != null) if (player.getExperience() > maxExperience) continue;
            if (minLevel != null) if (player.getLevel() < minLevel) continue;
            if (maxLevel != null) if (player.getLevel() > maxLevel) continue;
            count++;
        }

        return count;
    }

    public Player createPlayer(Data data) {
        Player player = new Player();
        player.setName(data.getName());
        player.setTitle(data.getTitle());
        player.setBanned(data.getBanned());
        player.setBirthday(data.getBirthday());
        player.setLevel((int) (Math.sqrt(2500 + data.getExperience() * 200) - 50) / 100);
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - data.getExperience());
        player.setProfession(data.getProfession());
        player.setRace(data.getRace());
        player.setExperience(data.getExperience());
        return playerRepository.save(player);
    }

    public Player getPlayer(Long id) {
        return playerRepository.findById(id).orElseThrow(ExceptionsNOT_FOUND::new);
    }

    public Player updatePlayer(Data data, Long id) {
        Player player = playerRepository.findById(id).orElseThrow(ExceptionsNOT_FOUND::new);
        if (data.getName() != null) player.setName(data.getName());
        if (data.getTitle() != null) player.setTitle(data.getTitle());
        if (data.getBanned() != null) player.setBanned(data.getBanned());
        if (data.getBirthday() != null) player.setBirthday(data.getBirthday());
        if (data.getExperience() != null) {
            player.setExperience(data.getExperience());
            player.setLevel((int) (Math.sqrt(2500 + data.getExperience() * 200) - 50) / 100);
            player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - data.getExperience());
        }
        if (data.getProfession() != null) player.setProfession(data.getProfession());
        if (data.getRace() != null) player.setRace(data.getRace());

        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        playerRepository.findById(id).orElseThrow(ExceptionsNOT_FOUND::new);
        playerRepository.deleteById(id);
    }
}
