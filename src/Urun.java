
public class Urun {
    private int id;
    private String ad;
    private double fiyat;
    private String kategori;
    private int adet;

    public Urun(int id, String ad, double fiyat, String kategori) 
    {
        this.id = id;
        this.ad = ad;
        this.fiyat = fiyat;
        this.kategori = kategori;
        this.adet = 0;
    }

    public int getId() 
    { 
    	return id; 
    }
    public String getAd() 
    { 
    	return ad; 
    }
    public double getFiyat() 
    { 
    	return fiyat; 
    }
    public String getKategori() 
    { 
    	return kategori; 
    }
    public int getAdet() 
    { 
    	return adet; 
    }
    public void setAdet(int adet) 
    { 
    	this.adet = adet; 
    }

    @Override
    public String toString() 
    {
        return ad + " - " + String.format("%.2f TL", fiyat);
    }
}