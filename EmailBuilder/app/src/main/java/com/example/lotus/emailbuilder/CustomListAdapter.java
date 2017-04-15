package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Lotus on 09/03/2017.
 */

//public class CustomListAdapter extends ArrayAdapter<String> implements Filterable{
public class CustomListAdapter extends BaseAdapter implements Filterable{

    private Activity context;
    //List<String> name;
    //List<String> alamat;
    //public ArrayList<Site> name;
    //public ArrayList<Site> alamat;
    public ArrayList<Site> employeeArrayList;
    public ArrayList<Site> orig;
    private Multipart _multipart = new MimeMultipart();

    Session session = null;
    ProgressDialog pdialog = null;

    String alamatEmail="";
    String judulEmail="";
    String isiEmail="";

    Template template = new Template();

    /*public CustomListAdapter(Activity context, List<String> name, List<String> alamat) {
        super(context, R.layout.activity_listview, name);
        this.context = context;
        this.name = name;
        this.alamat = alamat;
    }*/

    public CustomListAdapter(Activity context, ArrayList<Site> employeeArrayList) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }

    public class SiteHolder
    {
        TextView name;
        TextView alamat;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        /*LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_listview, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.description);

        txtTitle.setText(name.get(position));
        txtDesc.setText(alamat.get(position));

        return rowView;*/

        SiteHolder holder;
        if(view==null)
        {
            view=LayoutInflater.from(context).inflate(R.layout.activity_listview, parent, false);
            holder=new SiteHolder();
            holder.name=(TextView) view.findViewById(R.id.title);
            holder.alamat=(TextView) view.findViewById(R.id.description);
            view.setTag(holder);
        }
        else
        {
            holder=(SiteHolder) view.getTag();
        }

        holder.name.setText(employeeArrayList.get(position).getNama());
        holder.alamat.setText(employeeArrayList.get(position).getAlamat());

        Log.d("TAGES", employeeArrayList.get(position).getNama()+" "+ employeeArrayList.get(position).getEmail());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("TAGES", employeeArrayList.get(position).getNama());
                Intent intent = new Intent(context, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idsite", employeeArrayList.get(position).getSiteid());
                bundle.putString("name", employeeArrayList.get(position).getNama());
                bundle.putString("alamat", employeeArrayList.get(position).getAlamat());
                bundle.putString("email", employeeArrayList.get(position).getEmail());
                intent.putExtras(bundle);
                context.startActivity(intent);
                Log.d("TAGES", employeeArrayList.get(position).getSiteid()+" "+ employeeArrayList.get(position).getNama());

            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub

                alamatEmail = employeeArrayList.get(position).getEmail();
                judulEmail = template.judulEmail + employeeArrayList.get(position).getNama();
                isiEmail = template.isiEmail + employeeArrayList.get(position).getNama() + template.isiEmailTengah + employeeArrayList.get(position).getAlamat() + template.isiEmailBawah;
                Log.v("TAGES","pos: " + employeeArrayList.get(position).getNama());

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("alvinalbuquerque@gmail.com", "sayamaumakan");
                    }
                });

                pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

                SendingEmail task = new SendingEmail();
                task.execute();

                return true;
            }
        });

        return view;
    }

    class SendingEmail extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("alvinalbuquerque@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(alamatEmail));
                message.setSubject(judulEmail);

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(isiEmail);
                _multipart.addBodyPart(messageBodyPart);
                message.setContent(_multipart);

                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();
        }
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Site> results = new ArrayList<Site>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Site g : orig) {
                            if (g.getNama().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                employeeArrayList = (ArrayList<Site>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return employeeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        int itemID;

        // orig will be null only if we haven't filtered yet:
        if (orig == null)
        {
            itemID = position;
        }
        else
        {
            itemID = orig.indexOf(employeeArrayList.get(position));
        }
        return itemID;    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}