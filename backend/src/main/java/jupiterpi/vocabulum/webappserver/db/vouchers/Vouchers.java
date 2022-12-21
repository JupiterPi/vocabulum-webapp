package jupiterpi.vocabulum.webappserver.db.vouchers;

import jupiterpi.vocabulum.core.db.entities.EntityCollection;
import jupiterpi.vocabulum.core.db.entities.EntityProvider;
import jupiterpi.vocabulum.webappserver.db.WebappDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vouchers extends EntityCollection<Voucher> {
    public Vouchers(WebappDatabase webappDatabase) {
        super(EntityProvider.fromMongoCollection(webappDatabase.collection_vouchers));
        load();
    }

    @Override
    protected Voucher createEntityWithDocumentId(String documentId) {
        return Voucher.readEntity(entityProvider, documentId);
    }

    public List<Voucher> createVouchers(Date expiration, int amount, String note) {
        List<Voucher> vouchers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Voucher voucher = Voucher.createVoucher(expiration, note);
            vouchers.add(voucher);
            addEntity(voucher);
        }
        saveEntities();
        return vouchers;
    }

    public List<Voucher> getAll() {
        return new ArrayList<>(entities);
    }

    public Voucher getVoucher(String code) {
        return entities.stream()
                .filter(voucher -> voucher.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}