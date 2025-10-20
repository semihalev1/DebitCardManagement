
import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class HesapSecEkrani extends JDialog {

    private JTable tbl_hesaplar;
    private DefaultTableModel model;
    private List<Object[]> secilenHesaplar = new ArrayList<>();
    private long musteriId;
    private char type;
    private String cardId;

    public HesapSecEkrani(Frame parent, boolean modal, long musteriId, String cardId, char type) {
        super(parent, modal);
        this.musteriId = musteriId;
        this.type = type;
        this.cardId = cardId;
        initComponents();
        tabloyuDoldur(type);
    }

    private void initComponents() {
        setTitle("Hesap Seç");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"Para Birimi", "Hesap No", "Suffix"}, 0);
        tbl_hesaplar = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tbl_hesaplar);

        JButton btn_sec = new JButton("Seç");
        btn_sec.addActionListener(e -> {
            int[] selectedRows = tbl_hesaplar.getSelectedRows();
            if (selectedRows.length > 0) {
                for (int row : selectedRows) {
                    int columnCount = model.getColumnCount();
                    Object[] hesap = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        hesap[i] = model.getValueAt(row, i);
                    }
                    secilenHesaplar.add(hesap);
                }
                dispose(); // pencereyi kapat
            } else {
                
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(btn_sec, BorderLayout.SOUTH);
    }

    private void tabloyuDoldur(char type) {
        if (type == 'A') {
            CompanyDbMenagement.searchByCustomerIdMain(musteriId, (DefaultTableModel) tbl_hesaplar.getModel());
            tbl_hesaplar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            List<Long> virmanAccountNos = CompanyDbMenagement.getAccountNosByType('V');
            List<Long> mainAccountNos = CompanyDbMenagement.getAccountNosByType('A');
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                Object accountNoObj = model.getValueAt(i, 1); // 1 = hesap no sütunu
                if (accountNoObj != null) {
                    long accountNo = Long.parseLong(accountNoObj.toString());
                    if (mainAccountNos.contains(accountNo)) {
                        model.removeRow(i); // satırı sil
                    }
                }
            }
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                Object accountNoObj = model.getValueAt(i, 1); // 1 = hesap no sütunu
                if (accountNoObj != null) {
                    long accountNo = Long.parseLong(accountNoObj.toString());
                    if (virmanAccountNos.contains(accountNo)) {
                        model.removeRow(i); // satırı sil
                    }
                }
            }
        } else if (type == 'V') {
            CompanyDbMenagement.searchByCustomerId(musteriId, (DefaultTableModel) tbl_hesaplar.getModel());
            List<Long> mainAccountNos = CompanyDbMenagement.getAccountNosByType('A');
            List<Long> virmanAccountNos = CompanyDbMenagement.getAccountNosByType('V');

// Ters sırayla sil, index kayması olmaması için
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                Object accountNoObj = model.getValueAt(i, 1); // 1 = hesap no sütunu
                if (accountNoObj != null) {
                    long accountNo = Long.parseLong(accountNoObj.toString());
                    if (mainAccountNos.contains(accountNo)) {
                        model.removeRow(i); // satırı sil
                    }
                }
            }
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                Object accountNoObj = model.getValueAt(i, 1); // 1 = hesap no sütunu
                if (accountNoObj != null) {
                    long accountNo = Long.parseLong(accountNoObj.toString());
                    if (virmanAccountNos.contains(accountNo)) {
                        model.removeRow(i); // satırı sil
                    }
                }
            }
        } else if (type == 'E') {
            CompanyDbMenagement.searchVirmanAccountsByCardIdan(cardId, (DefaultTableModel) tbl_hesaplar.getModel());

        }
    }

    public List<Object[]> getSecilenHesaplar() {
        return secilenHesaplar;
    }
}
