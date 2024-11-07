package com.example.horoscopo.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopo.R
import com.example.horoscopo.adapters.HoroscopeAdapters
import com.example.horoscopo.data.Horoscope
import com.example.horoscopo.data.HoroscopeProvider

class MainActivity : AppCompatActivity() {

    // La lista de goroscopos a mostrar
    lateinit var horoscopeList: List<Horoscope>

    // La referencia del RecyclerView
    lateinit var recyclerView: RecyclerView

    //El adapter para decurle al RecyclerView que datos queremos
    lateinit var adapter:HoroscopeAdapters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //Buscamos el RecyclerView en la vista
        recyclerView = findViewById(R.id.recyclerView)

        // Obtenemos el listado de horóscopos
        horoscopeList = HoroscopeProvider.findAll()

        // Creamos el adapter pasandole la lista de horoscopos y la función lamda para cuando  se haga click en uno
        adapter = HoroscopeAdapters(horoscopeList) { position ->
            val horoscope = horoscopeList[position]
            println("HOROSOCOPOOOOOO \ud83d\uDE03 ${getString(horoscope.name)}")
            navigateToDetail(horoscope)
        }

        // Asignamos el adapter al RecyclerView y le decimos que muestre las celdas verticalmente
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }

    override fun onResume() {
        super.onResume()

        // Refrescamos la lista notificnado al adapter de que los datos han cambiado
        adapter.notifyDataSetChanged() // Esto lo hacemos para que refleje el favorito cuando cambie
    }

    // función para mostrar el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_list_activity, menu)

        // Busco la opción de búsqueda en el menú
        val searchMenuItem = menu?.findItem((R.id.menu_search))!!

        //Obtengo la clase del ActionView asociada a esa opción del menú
        val searchView = searchMenuItem.actionView as SearchView

        // Le asigno el listener a la busqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            // Función para cuando se pulsa enter
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            // Función para cada vez que cambia el texto
            override fun onQueryTextChange(newText: String): Boolean {
                // Filtro la lista en base al nombre y las fechas del horóscopo
                horoscopeList = HoroscopeProvider.findAll().filter {
                    getString(it.name).contains(newText, true) ||
                            getString(it.dates).contains(newText, true)
                }

                // Le paso la nueva lista al adapter
                adapter.updateItems(horoscopeList)
                return true
            }
        })
        return  true

        }


    // Navegar a DetailActivity pasandole el id horóscopo seleccionado
    private fun navigateToDetail(horoscope: Horoscope) {
        val intent = Intent(this,DetailActivity::class.java)
        intent.putExtra("horoscope_id", horoscope.id)
        startActivity(intent)

    }
}