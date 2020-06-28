package com.dummy.myerp.testconsumer.consumer;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ConsumerIntegrationtest extends ConsumerTestCase {

    @Test
    public void getListCompteComptable() {

        List<CompteComptable> cptliste = getDaoProxy().getComptabiliteDao().getListCompteComptable();
        assertNotNull(cptliste);

    }

    @Test
    public void getListJournalComptable() {
        List<JournalComptable> jrnliste = getDaoProxy().getComptabiliteDao().getListJournalComptable();
        assertNotNull(jrnliste);

    }

    @Test
    public void getListEcritureComptable() {
        List<EcritureComptable> ecriturelist = getDaoProxy().getComptabiliteDao().getListEcritureComptable();
        assertNotNull(ecriturelist);

    }

    @Test
    public void getEcritureComptable() throws ParseException {

        EcritureComptable ecriturecpt;
        try {
            ecriturecpt = getDaoProxy().getComptabiliteDao().getEcritureComptable(-2);
            assertEquals("VE", ecriturecpt.getJournal().getCode());
            assertEquals("VE-2016/00002", ecriturecpt.getReference());
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format.parse("30-12-2016");
            assertEquals(date, ecriturecpt.getDate());
            assertEquals("TMA Appli Xxx", ecriturecpt.getLibelle());
            assertNotNull(ecriturecpt.getListLigneEcriture());
            assertNotNull(ecriturecpt);
        } catch (NotFoundException e) {
            System.out.println("ecriture comptable introuvable");
            e.printStackTrace();
        }
    }


    @Test
    public void getEcritureComptableByRef() throws ParseException {

        EcritureComptable ecriturecpt;
        try {
            ecriturecpt = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef("VE-2016/00002");
            assertNotNull(ecriturecpt);
            assertEquals("VE", ecriturecpt.getJournal().getCode());
            assertEquals("VE-2016/00002", ecriturecpt.getReference());
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format.parse("30-12-2016");
            assertEquals(date, ecriturecpt.getDate());
            assertEquals("TMA Appli Xxx", ecriturecpt.getLibelle());
            assertNotNull(ecriturecpt.getListLigneEcriture());
        } catch (NotFoundException e) {
            System.out.println("ecriture comptable non trouvee");
            e.printStackTrace();
        }
    }

    @Test
    public void loadListLigneEcriture() throws NotFoundException {
        EcritureComptable ecr = getDaoProxy().getComptabiliteDao().getEcritureComptable(-3);
        getDaoProxy().getComptabiliteDao().loadListLigneEcriture(ecr);
        assertEquals(401, (int) ecr.getListLigneEcriture().get(0).getCompteComptable().getNumero());
        assertEquals(512, (int) ecr.getListLigneEcriture().get(1).getCompteComptable().getNumero());
    }

    @Test
    public void insertEcritureComptable() {

        List<EcritureComptable> l = getDaoProxy().getComptabiliteDao().getListEcritureComptable();
        EcritureComptable e = l.get(l.size() - 1);
        int v = Integer.parseInt(e.getReference().substring(8)) + 1;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy");
        int annee = Integer.parseInt(formater.format(e.getDate()));
        e.setReference(e.getJournal().getCode() + "-" + annee + "/" + String.format("%05d", v));
        e.setJournal(new JournalComptable("AC", "Achat"));
        e.setLibelle("testajoutdao");
        e.setDate(new Date());
        e.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(200),
                null));
        e.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(200)));
        getDaoProxy().getComptabiliteDao().insertEcritureComptable(e);
        assertNotNull(e.getId());
    }

    @Test
    public void updateEcritureComptable() {

        List<EcritureComptable> vEcritureComptableList = getDaoProxy().getComptabiliteDao().getListEcritureComptable();
        for (EcritureComptable vEcritureComptable : vEcritureComptableList) {
            if (vEcritureComptable.getId() == -3) {
                vEcritureComptable.setLibelle("EXP");
                getDaoProxy().getComptabiliteDao().updateEcritureComptable(vEcritureComptable);
                assertEquals("Mise a jour reussie", "EXP", vEcritureComptable.getLibelle());
            }
        }

    }


    @Test
    public void deleteEcritureComptable() throws ParseException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(-2);
        vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vEcritureComptable.setDate(pattern.parse("2016-12-30 00:00:00"));
        vEcritureComptable.setLibelle("TMA Appli Xxx");
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String refYear = df.format(vEcritureComptable.getDate());
        vEcritureComptable.setReference(vEcritureComptable.getJournal().getCode() + "-" + refYear + "/00002");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(200),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(200)));

        int sizeinit = getDaoProxy().getComptabiliteDao().getListEcritureComptable().size();
        getDaoProxy().getComptabiliteDao().deleteEcritureComptable(vEcritureComptable.getId());
        int sizefinal = getDaoProxy().getComptabiliteDao().getListEcritureComptable().size();
        assertEquals(sizeinit - 1, sizefinal);
    }


    @Test
    public void getDerniereSequenceEcritureComptable() {
        assertEquals("SequenceEcritureComptable{annee=2016, derniereValeur=51}", getDaoProxy().getComptabiliteDao().getLastSequenceEcritureComptable("BQ",2016).toString());

    }


    @Test
    public void insertSequenceEcritureComptable() throws NotFoundException {

        SequenceEcritureComptable seq = new SequenceEcritureComptable();
        EcritureComptable e;
        String codej;

        //insert
        seq.setAnnee(2018);
        seq.setDerniereValeur(100);
        e = getDaoProxy().getComptabiliteDao().getEcritureComptable(-3);
        codej = e.getJournal().getCode();
        seq.setJournalCode(codej);
        getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(seq);

        //update
        seq = new SequenceEcritureComptable();
        seq.setAnnee(2017);
        seq.setDerniereValeur(41);
        e = getDaoProxy().getComptabiliteDao().getEcritureComptable(-3);
        codej = e.getJournal().getCode();
        seq.setJournalCode(codej);
        getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(seq);
        assertEquals("SequenceEcritureComptable{annee=2017, derniereValeur=41}", getDaoProxy().getComptabiliteDao().getLastSequenceEcritureComptable("BQ",2017).toString());
    }
}
