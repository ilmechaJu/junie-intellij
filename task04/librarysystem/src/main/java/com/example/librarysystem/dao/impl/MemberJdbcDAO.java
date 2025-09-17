package com.example.librarysystem.dao.impl;

import com.example.librarysystem.dao.MemberDAO;
import com.example.librarysystem.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberJdbcDAO implements MemberDAO {
    private final Connection connection;

    public MemberJdbcDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Member save(Member member) {
        try {
            if (member.getId() == null) {
                String sql = "INSERT INTO members(name, email, phone) VALUES(?,?,?)";
                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, member.getName());
                    ps.setString(2, member.getEmail());
                    ps.setString(3, member.getPhone());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) member.setId(rs.getLong(1));
                    }
                }
            } else {
                String sql = "UPDATE members SET name=?, email=?, phone=? WHERE id=?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, member.getName());
                    ps.setString(2, member.getEmail());
                    ps.setString(3, member.getPhone());
                    ps.setLong(4, member.getId());
                    ps.executeUpdate();
                }
            }
            return member;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, name, email, phone FROM members WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, name, email, phone FROM members ORDER BY id";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<Member> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM members WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Member map(ResultSet rs) throws SQLException {
        return Member.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .build();
    }
}
