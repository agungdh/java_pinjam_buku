/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

/**
 *
 * @author user
 */
@Table("pengembalian")
@BelongsToParents({
    @BelongsTo(parent = SiswaModel.class, foreignKeyName = "id_siswa"),
    @BelongsTo(parent = BukuModel.class, foreignKeyName = "id_buku")
})
public class PengembalianModel extends Model {}