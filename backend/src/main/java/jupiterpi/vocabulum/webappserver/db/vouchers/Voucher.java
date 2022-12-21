package jupiterpi.vocabulum.webappserver.db.vouchers;

import jupiterpi.vocabulum.core.db.entities.Entity;
import jupiterpi.vocabulum.core.db.entities.EntityProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.Document;

import java.util.Date;

public class Voucher extends Entity {
    private String code;
    private Date expiration;
    private String usedBy;
    private Date usedAt;
    private String note;

    public Voucher(String code, Date expiration, String usedBy, Date usedAt, String note) {
        this.code = code;
        this.expiration = expiration;
        this.usedBy = usedBy;
        this.usedAt = usedAt;
        this.note = note;
    }

    public static Voucher createVoucher(Date expiration, String note) {
        return new Voucher(
                RandomStringUtils.randomAlphanumeric(8),
                expiration,
                "", null, note
        );
    }

    /* getters */

    public String getCode() {
        return code;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public Date getUsedAt() {
        return usedAt;
    }

    public String getNote() {
        return note;
    }

    /* modifiers */

    public void useAndSave(String usedBy, Date usedAt) {
        this.usedBy = usedBy;
        this.usedAt = usedAt;
        saveEntity();
    }

    /* Entity */

    private Voucher(EntityProvider entityProvider, String documentId) {
        super(entityProvider, documentId);
    }
    public static Voucher readEntity(EntityProvider entityProvider, String documentId) {
        return new Voucher(entityProvider, documentId);
    }

    @Override
    protected void loadFromDocument(Document document) {
        code = document.getString("code");
        expiration = document.getDate("expiration");
        usedBy = document.getString("usedBy");
        usedAt = document.getDate("usedAt");
        note = document.getString("note");
    }

    @Override
    protected Document toDocument() {
        Document document = new Document();
        document.put("code", code);
        document.put("expiration", expiration);
        document.put("usedBy", usedBy);
        document.put("usedAt", usedAt);
        document.put("note", note);
        return document;
    }
}