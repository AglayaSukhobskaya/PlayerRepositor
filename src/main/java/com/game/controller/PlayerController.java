package com.game.controller;

import com.game.entity.Data;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exceptions.ExceptionsBAD_REQUEST;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {
    @Autowired
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getPlayersList(String name,
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
        return playerService.getPlayerList(name, title, race, profession,
                after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel, order, pageNumber, pageSize);
    }

    @GetMapping("/count")
    public Long getPlayersCount(String name,
                                   String title,
                                   Race race,
                                   Profession profession,
                                   Long after,
                                   Long before,
                                   Boolean banned,
                                   Integer minExperience,
                                   Integer maxExperience,
                                   Integer minLevel,
                                   Integer maxLevel) {

        return playerService.getPlayersCount(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    @PostMapping
    public Player createPlayer(@RequestBody Data data) {
        if (data.getName() == null ||
                data.getTitle() == null ||
                data.getBirthday() == null ||
                data.getExperience() == null ||
                data.getProfession() == null ||
                data.getRace() == null) throw new ExceptionsBAD_REQUEST();
        if (data.getName().length() > 12 || data.getTitle().length() > 30) throw new ExceptionsBAD_REQUEST();
        if (data.getName() == "") throw new ExceptionsBAD_REQUEST();
        if (data.getBirthday().getTime() < 0) throw new ExceptionsBAD_REQUEST();
        if (data.getExperience() < 0 || data.getExperience() > 10000000) throw new ExceptionsBAD_REQUEST();
        Date date1 = new Date(100, 0, 1);
        Date date2 = new Date(1100, 0, 1);
        if (data.getBirthday().getYear() < date1.getYear() || data.getBirthday().getYear() > date2.getYear())
            throw new ExceptionsBAD_REQUEST();
        return playerService.createPlayer(data);
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) {
        if (id < 1) throw new ExceptionsBAD_REQUEST();
        return playerService.getPlayer(id);
    }

    @PostMapping("/{id}")
    public Player updatePlayer(@RequestBody Data data, @PathVariable Long id) {
        if (id < 1) throw new ExceptionsBAD_REQUEST();

        if (data.getName() != null) {
            if (data.getName().length() > 12) throw new ExceptionsBAD_REQUEST();
            if (data.getName() == "") throw new ExceptionsBAD_REQUEST();
        }
        if (data.getTitle() != null) {
            if (data.getTitle().length() > 30) throw new ExceptionsBAD_REQUEST();
            if (data.getTitle() == "") throw new ExceptionsBAD_REQUEST();
        }
        if (data.getBirthday() != null) {
            if (data.getBirthday().getTime() < 0) throw new ExceptionsBAD_REQUEST();
            Date date1 = new Date(100, 0, 1);
            Date date2 = new Date(1100, 0, 1);
            if (data.getBirthday().getYear() < date1.getYear() || data.getBirthday().getYear() > date2.getYear())
                throw new ExceptionsBAD_REQUEST();
        }
        if (data.getExperience() != null)
            if (data.getExperience() < 0 || data.getExperience() > 10000000) throw new ExceptionsBAD_REQUEST();
        return playerService.updatePlayer(data, id);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        if (id < 1) throw new ExceptionsBAD_REQUEST();
        playerService.deletePlayer(id);
    }
}
