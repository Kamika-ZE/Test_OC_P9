package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class BusinessIntegrationTest extends BusinessTestCase {

    private ComptabiliteManagerImpl comptabiliteManager = new ComptabiliteManagerImpl();



    @Test
    public void getListCompteComptableTest() {
        List<CompteComptable> compteComptables = this.comptabiliteManager.getListCompteComptable();
        assertEquals(7, compteComptables.size());
    }

    @Test
    public void getListJournalComptableTest() {
        List<JournalComptable> journalComptables = this.comptabiliteManager.getListJournalComptable();
        assertEquals(4, journalComptables.size());
    }



    @Test
    public void checkEcritureComptable() throws FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achats"));
        vEcritureComptable.setDate(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String yearRef = sdf.format(vEcritureComptable.getDate());
        vEcritureComptable.setReference("AC-" + yearRef + "/00001");
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));


        comptabiliteManager.checkEcritureComptable(vEcritureComptable);


    }

}